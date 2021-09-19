package qunar.tc.bistoury.agent.common.kv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qunar.tc.bistoury.clientside.common.store.BistouryStore;
import qunar.tc.bistoury.common.FileUtil;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 肖哥弹架构
 * @date 2022-09-12
 * @desc KV数据库装饰者,用于数据库类型选择
 */
public class KvDbWrapper implements KvDb {
    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(KvDbWrapper.class);
    /**
     * 默认存储周期3天(259200秒) 线程监控数据
     */
    private static final int DEFAULT_TTL = (int) TimeUnit.DAYS.toSeconds(3);

    /**
     * rocksdb数据库
     */
    private static final String ROCKS_DB = "rocksdb";
    /**
     * sqlite数据库
     */
    private static final String SQLITE = "sqlite";
    /**
     * 默认最大压缩比为3
     */
    private static final int DEFAULT_MAX_COMPACTIONS = 3;
    /**
     * KVDB数据库实例
     */
    private final KvDb kvdb;

    /**
     * 构造方法
     */
    public KvDbWrapper() {
        //获取数据库类型，默认为rocksdb
        final String dbType = System.getProperty("bistoury.store.db", ROCKS_DB);
        //数据库指定类型为sqlite
        if (SQLITE.equalsIgnoreCase(dbType)) {
            final String rocksDbPath = BistouryStore.getStorePath(ROCKS_DB);
            final File file = new File(rocksDbPath);
            if (file.exists()) {
                LOG.info("clean rocksDb data, path:{}", file.getPath());
                FileUtil.deleteDirectory(file, true);
            }
            //创建sqlite存储实例，默认存储3天
            kvdb = new SQLiteStoreImpl(BistouryStore.getStorePath(SQLITE), DEFAULT_TTL);
        } else {
            //数据库类型为rocksdb,则创建RocksDB存储实例，默认存储3天，并压缩
            kvdb = new RocksDBStoreImpl(BistouryStore.getStorePath(ROCKS_DB), DEFAULT_TTL, DEFAULT_MAX_COMPACTIONS);
        }

    }

    /**
     * 获取数据
     * @param key 键
     * @return 值
     */
    @Override
    public String get(String key) {
        return kvdb.get(key);
    }

    /**
     * 存储数据
     * @param key 键
     * @param value 值
     */
    @Override
    public void put(String key, String value) {
        kvdb.put(key, value);
    }

    /**
     * 批量存储
     * @param data 数据
     */
    @Override
    public void putBatch(Map<String, String> data) {
        kvdb.putBatch(data);
    }
}
