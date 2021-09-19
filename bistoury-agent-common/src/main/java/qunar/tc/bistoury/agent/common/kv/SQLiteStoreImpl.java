package qunar.tc.bistoury.agent.common.kv;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.core.Codes;
import org.xerial.snappy.Snappy;
import qunar.tc.bistoury.clientside.common.meta.MetaStore;
import qunar.tc.bistoury.clientside.common.meta.MetaStores;
import qunar.tc.bistoury.common.FileUtil;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 肖哥弹架构
 * @date 2022-09-12
 * @desc KVSQLite数据库存储管理
 *
 * bistoury表结构
 * --------------------------------------------
 * |b_key|b_value|b_expire_time|b_compress_way|
 * --------------------------------------------
 */
public class SQLiteStoreImpl implements KvDb {
    /**
     * 日志管理
     */
    private static final Logger logger = LoggerFactory.getLogger(SQLiteStoreImpl.class);
    /**
     * 创建bistoury表
     */
    private static final String INIT_TABLE_SQL = "CREATE TABLE IF NOT EXISTS bistoury (" +
                                                                                        "b_key VARCHAR(200) NOT NULL PRIMARY KEY DEFAULT ''," +
                                                                                        "b_value BLOB NOT NULL DEFAULT ''," +
                                                                                        "b_expire_time BIGINT NOT NULL DEFAULT 0," +
                                                                                        "b_compress_way TINYTEXT NOT NULL DEFAULT 0" +
                                                                                        ");";
    /**
     * 为bistoury表创建索引
     */
    private static final String CREATE_INDEX_SQL = "CREATE INDEX idx_b_expire_time ON bistoury (b_expire_time);";
    /**
     * 为bistoury表插入数据
     */
    private static final String INSERT_SQL = "insert into bistoury values (?, ?, ?, ?)";
    /**
     * 更新bistoury表数据
     */
    private static final String UPDATE_SQL = "update bistoury set b_value=?,b_expire_time=?,b_compress_way=? where b_key=?";
    /**
     * 查询bistoury表数据
     */
    private static final String SELECT_SQL = "select b_value, b_compress_way from bistoury where b_key = ?";
    /**
     * 查询bistoury表过期数据
     */
    private static final String QUERY_EXPIRE_KEY = "select b_key from bistoury where b_expire_time <= ? limit 0,?";
    /**
     * bistoury数据库存储文件
     */
    private static final String db_file = "bistoury.db";
    /**
     * bistoury.db存储路径
     */
    private String path;
    /**
     * 文件有效期多少秒
     */
    private long ttl;
    /**
     * 元数据存储
     */
    private final MetaStore metaStore;
    /**
     * private final DataSource dataSource;
     */
    private Connection connection;

    /**
     * 构造方法
     * @param path
     * @param ttl  Unit s
     */
    public SQLiteStoreImpl(String path, long ttl) {
        //确认存储路径存在
        FileUtil.ensureDirectoryExists(path);
        //存储多少秒
        this.ttl = TimeUnit.SECONDS.toMillis(ttl);
        // bistoury.db存储路径
        this.path = FileUtil.dealPath(path, db_file);
        //获取元数据存储
        this.metaStore = MetaStores.getMetaStore();
        //初始化
        init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        //bistoury.db文件存在则标记已初始化
        boolean isInit = false;
        final File file = new File(this.path);
        if (file.exists()) {
            isInit = true;
        }
        try {
            //创建数据库连接
            connection = DriverManager.getConnection("jdbc:sqlite:" + this.path);
            if (!isInit) {
                //声明对象,并创建表与索引
                try (Statement stmt = connection.createStatement()) {
                    stmt.executeUpdate(INIT_TABLE_SQL);
                    stmt.executeUpdate(CREATE_INDEX_SQL);
                    stmt.execute("PRAGMA journal_mode=WAL;");
                }
            } else {
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("PRAGMA journal_mode=WAL;");
                }
            }
            logger.info("init sqlite db success, path: {}, ttl: {}", this.path, ttl);
        } catch (Exception e) {
            logger.error("init sqlite db error, path: {}, ttl: {}", this.path, ttl, e);
            throw new RuntimeException(e);
        }

        final SQLiteDeleteDataGentle sqLiteDeleteDataGentle = new SQLiteDeleteDataGentle(this);
                                     sqLiteDeleteDataGentle.start();

        Runtime.getRuntime().addShutdownHook(new Thread("sqlite resource claen") {
            @Override
            public void run() {
                try {
                    connection.close();
                } catch (Exception e) {
                    logger.error("close connection fail", e);
                }
                sqLiteDeleteDataGentle.destroy();
            }
        });
    }

    /**
     * 根据表key字段获取sqlite数据
     * @param key b_key表字段值
     * @return b_value字段值
     */
    @Override
    public String get(String key) {
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        synchronized (connection) {
            try {
                pstmt = connection.prepareStatement(SELECT_SQL);
                pstmt.setString(1, key);
                resultSet = pstmt.executeQuery();
                while (resultSet.next()) {
                    final byte[] value = resultSet.getBytes("b_value");
                    final int way = resultSet.getInt("b_compress_way");
                    if (value != null) {
                        return unCompress(value, way);
                    }
                }
                return null;
            } catch (Exception e) {
                logger.error("get value fail, key: {}", key, e);
                return null;
            } finally {
                DataSourceHelper.closeResource(resultSet, pstmt);
            }
        }
    }

    /**
     * 存储记录
     * @param key  b_key表字段值
     * @param value b_value字段值
     */
    @Override
    public void put(String key, String value) {
        PreparedStatement pstmt = null;
        final CompressData compressData = compress(value);
        final long expire_time = System.currentTimeMillis() + this.ttl;
        synchronized (connection) {
            try {
                pstmt = connection.prepareStatement(INSERT_SQL);
                pstmt.setString(1, key);
                pstmt.setBytes(2, compressData.getData());
                pstmt.setLong(3, expire_time);
                pstmt.setInt(4, compressData.getWay());
                pstmt.executeUpdate();
            } catch (SQLException sqe) {
                if (sqe.getErrorCode() == Codes.SQLITE_CONSTRAINT) {
                    update(key, value);
                } else {
                    logger.error("insert into sqlite fail, key: {}, value; {}", key, value, sqe);
                }
            } catch (Exception e) {
                logger.error("insert into sqlite fail, key: {}, value; {}", key, value, e);
            } finally {
                DataSourceHelper.closeResource(pstmt);
            }
        }
    }
    /**
     * 更新记录
     * @param key  b_key表字段值
     * @param value b_value字段值
     */
    public void update(String key, String value) {
        PreparedStatement pstmt = null;
        final CompressData compressData = compress(value);
        final long expire_time = System.currentTimeMillis() + this.ttl;
        synchronized (connection) {
            try {
                pstmt = connection.prepareStatement(UPDATE_SQL);
                pstmt.setBytes(1, compressData.getData());
                pstmt.setLong(2, expire_time);
                pstmt.setInt(3, compressData.getWay());
                pstmt.setString(4, key);
                pstmt.executeUpdate();
            } catch (Exception e) {
                logger.error("insert into sqlite fail, key: {}, value; {}", key, value, e);
            } finally {
                DataSourceHelper.closeResource(pstmt);
            }
        }
    }
    /**
     * 批量插入记录
     * @param data 批量记录
     **/
    @Override
    public void putBatch(Map<String, String> data) {
        try {
            //获取批量插入的数
            final int batchSize = metaStore.getIntProperty("sqlite.batch.save.size", 100);
            //获取批量存储后休眠毫秒
            final long batchSleep = metaStore.getLongProperty("sqlite.batch.save.sleep.ms", 100);
            List<Map.Entry<String, String>> batch = new ArrayList<>(batchSize);

            int count = 0;
            for (Map.Entry<String, String> entry : data.entrySet()) {
                batch.add(entry);
                if (++count >= batchSize) {
                    doPutBatch(batch);
                    batch.clear();
                    count = 0;
                    try {
                        Thread.sleep(batchSleep);
                    } catch (InterruptedException e) {
                        logger.error("batch save fail, data: {}", data, e);
                        break;
                    }
                }
            }
            if (!batch.isEmpty()) {
                doPutBatch(batch);
            }
        } catch (Exception e) {
            logger.error("batch insert into sqlite fail, data: {}", data, e);
        }
    }

    /**
     * 批量插入记录
     * insert into bistoury values (?, ?, ?, ?)
     * @param batch 批量记录实体
     */
    private void doPutBatch(List<Map.Entry<String, String>> batch) {
        PreparedStatement pstmt = null;
        boolean autoCommit = true;
        final long expire_time = System.currentTimeMillis() + this.ttl;
        synchronized (connection) {
            try {
                autoCommit = connection.getAutoCommit();
                connection.setAutoCommit(false);

                pstmt = connection.prepareStatement(INSERT_SQL);
                for (Map.Entry<String, String> entry : batch) {
                    final CompressData compressData = compress(entry.getValue());
                    pstmt.clearParameters();
                    pstmt.setString(1, entry.getKey());
                    pstmt.setBytes(2, compressData.getData());
                    pstmt.setLong(3, expire_time);
                    pstmt.setInt(4, compressData.getWay());
                    pstmt.addBatch();//添加批次
                }
                pstmt.executeBatch();

                connection.commit();
            } catch (Exception e) {
                logger.error("batch insert into sqlite fail, data: {}", batch, e);
            } finally {
                try {
                    connection.setAutoCommit(autoCommit);
                } catch (Exception e) {
                    logger.error("set auto commit fail, {}", autoCommit, e);
                }
                DataSourceHelper.closeResource(pstmt);
            }
        }
    }

    /**
     * 删除指定键记录
     * delete from bistoury where b_key in (?,?,?)
     * @param keys 被删除键
     * @return 删除记录数
     */
    public int delete(List<String> keys) {
        PreparedStatement pstmt = null;

        String deleteSql = getDeleteSql(keys);
        if (Strings.isNullOrEmpty(deleteSql)) {
            return 0;
        }
        synchronized (connection) {
            try {
                pstmt = connection.prepareStatement(deleteSql);
                return pstmt.executeUpdate();
            } catch (Exception e) {
                logger.error("clean expire data fail", e);
                return 0;
            } finally {
                DataSourceHelper.closeResource(pstmt);
            }
        }
    }

    /**
     * 获取bistoury表所有过期
     * select b_key from bistoury where b_expire_time <= ? limit 0,?
     * @param expireTimestamp 过期时间
     * @param limit 限制返回记录条数
     * @return 过期b_key列表
     */
    public List<String> expireKey(long expireTimestamp, int limit) {
        List<String> res = Lists.newArrayList();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        synchronized (connection) {
            try {
                pstmt = connection.prepareStatement(QUERY_EXPIRE_KEY);
                pstmt.setLong(1, expireTimestamp);
                pstmt.setInt(2, limit);
                resultSet = pstmt.executeQuery();
                while (resultSet.next()) {
                    res.add(resultSet.getString("b_key"));
                }
                return res;
            } catch (Exception e) {
                logger.error("get expire key fail", e);
                return res;
            } finally {
                DataSourceHelper.closeResource(resultSet, pstmt);
            }
        }
    }

    /**
     * 构建批量删除记录语法
     * delete from bistoury where b_key in (?,?,?)
     * @param keys 被删除的键值
     * @return 删除语法
     */
    private static String getDeleteSql(List<String> keys) {
        List<String> newKeys = Lists.transform(keys, new Function<String, String>() {
            @Override
            public String apply(String input) {
                return "'" + input + "'";
            }
        });
        StringBuilder sb = new StringBuilder("delete from bistoury where b_key in (");

        Joiner joiner = Joiner.on(",").skipNulls();

        HashSet<String> set = Sets.newHashSet(newKeys);
        if (set.size() == 0) {
            return null;
        }

        joiner.appendTo(sb, set);
        sb.append(")");
        return sb.toString();
    }

    /**
     * 压缩
     * @param data 被压缩数据
     * @return 压缩后的数据实体
     */
    private CompressData compress(final String data) {
        if (Strings.isNullOrEmpty(data)) {
            return new CompressData(CompressWay.NONE.way, new byte[0]);
        }
        CompressWay compressWay = getCompressWay(data);
        try {
            byte[] bytes = compressWay.compress.apply(data);
            return new CompressData(compressWay.way, bytes);
        } catch (Exception e) {
            logger.warn("use {} compress data fail, using raw data", compressWay.name(), e);
            CompressWay noneCompress = CompressWay.NONE;
            byte[] bytes = noneCompress.compress.apply(data);
            return new CompressData(noneCompress.way, bytes);
        }
    }

    /**
     * 解压
     * @param data 压缩数据
     * @param way 压缩方式
     * @return 解压后的数据
     */
    private String unCompress(final byte[] data, final int way) {
        if (data == null) {
            return null;
        }
        if (data.length == 0) {
            return "";
        }
        CompressWay compressWay = CompressWay.valueOf(way);
        try {
            return compressWay.uncompress.apply(data);
        } catch (Exception e) {
            logger.error("use {} uncompress data fail", compressWay.name(), e);
            return null;
        }
    }

    /**
     * 获取压缩枚举类型,字符串少于500则不压缩
     * @param data 需要压缩的数据
     * @return 压缩方式
     */
    private CompressWay getCompressWay(final String data) {
        if (data.length() < 500) {
            return CompressWay.NONE;
        }
        return CompressWay.SNAPPY;
    }

    /**
     * 压缩数据实体
     */
    private class CompressData {
        /**
         * 压缩数据方式
         */
        private int way;
        /**
         * 压缩数据
         */
        private byte[] data;

        public CompressData(int way, byte[] data) {
            this.way = way;
            this.data = data;
        }

        public int getWay() {
            return way;
        }

        public byte[] getData() {
            return data;
        }
    }

    /**
     * 压缩枚举
     */
    private enum CompressWay {
        NONE(0, new Function<String, byte[]>() {
            @Override
            public byte[] apply(String input) {
                return input.getBytes(Charsets.UTF_8);
            }
        }, new Function<byte[], String>() {
            @Override
            public String apply(byte[] input) {
                return new String(input, Charsets.UTF_8);
            }
        }),

        SNAPPY(1, new Function<String, byte[]>() {
            @Override
            public byte[] apply(String input) {
                try {
                    return Snappy.compress(input);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Function<byte[], String>() {
            @Override
            public String apply(byte[] input) {
                try {
                    return new String(Snappy.uncompress(input), Charsets.UTF_8);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        /**
         * 压缩方式(0,1)
         */
        private int way;
        /**
         * 压缩函数
         */
        private Function<String, byte[]> compress;
        /**
         * 解压函数
         */
        private Function<byte[], String> uncompress;

        CompressWay(int way, Function<String, byte[]> compress, Function<byte[], String> uncompress) {
            this.way = way;
            this.compress = compress;
            this.uncompress = uncompress;
        }

        private static final HashMap<Integer, CompressWay> ways = new HashMap<>(values().length);

        static {
            for (CompressWay value : CompressWay.values()) {
                ways.put(value.way, value);
            }
        }


        public static CompressWay valueOf(int way) {
            return ways.get(way);
        }
    }
}
