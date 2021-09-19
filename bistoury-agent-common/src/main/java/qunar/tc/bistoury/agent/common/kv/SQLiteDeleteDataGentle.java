package qunar.tc.bistoury.agent.common.kv;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qunar.tc.bistoury.clientside.common.meta.MetaStore;
import qunar.tc.bistoury.clientside.common.meta.MetaStores;
import qunar.tc.bistoury.common.NamedThreadFactory;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author 肖哥弹架构
 * @date 2022-09-12
 * @desc 优雅删除SQLITE数据
 */
public class SQLiteDeleteDataGentle {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(SQLiteDeleteDataGentle.class);
    /**
     * SQLITE存储管理组件
     */
    private SQLiteStoreImpl sqLite;

    /**
     * Time to execute cleanup task, used for the 24-hour clock.
     * 定期清理数据时间点
     */
    private static final int EXECUTE_CLEAN_DATA_HOUR = 3;
    /**
     * 清理过期定期线程池
     */
    private static final ListeningScheduledExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("clean_sqlite_expire_data")));
    /**
     * 元数据存储管理组件
     */
    private static final MetaStore META_STORE = MetaStores.getMetaStore();

    public SQLiteDeleteDataGentle(SQLiteStoreImpl sqLite) {
        this.sqLite = sqLite;
    }

    /**
     * 开启删除线程
     */
    public void start() {
        //开启定时删除线程任务（1小时执行一次）
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                Calendar calendar = new GregorianCalendar();
                //每日凌晨3点执行删除任务
                if (calendar.get(Calendar.HOUR_OF_DAY) == EXECUTE_CLEAN_DATA_HOUR) {
                    //开始删除时间
                    long start = System.currentTimeMillis();
                    //删除过期记录
                    int delete = deleteGentle();
                    logger.info("finish delete expire data, count: {}, cost: {}", delete, System.currentTimeMillis() - start);
                }
            }
        }, 1, 1, TimeUnit.HOURS);
    }


    /**
     * 削峰（删除过期数据）
     * @return
     */
    private int deleteGentle() {
        //将当前时间作为过期时间
        long expireTimestamp = System.currentTimeMillis();
        int count = 0;

        while (true) {
            //每批删除记录上限
            int limit = META_STORE.getIntProperty("delete.each.query.limit", 10000);
            //每批删除多少条记录
            int slice = META_STORE.getIntProperty("delete.slice.size", 100);
            //批量删除后休眠间隔
            long sleepTime = META_STORE.getLongProperty("delete.sleep.ms", 100);

            //一次查询 $limit 条key
            List<String> keySet = sqLite.expireKey(expireTimestamp, limit);

            if (keySet.size() == 0) {
                break;
            }

            if (limit == slice) {
                //如果分片数量和查询总数一样，就直接删除，跳过分组逻辑
                count += doDelete(keySet);
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    break;
                }
            } else {
                //将这个 $limit 条数据进行分组
                List<List<String>> partition = Lists.partition(keySet, slice);
                for (List<String> keys : partition) {
                    count += doDelete(keys);
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }

            if (keySet.size() < limit) {
                break;
            }
        }
        return count;
    }

    /**
     * 删除指定键记录
     * @param keys 被删除记录键列表
     * @return 被删除记录条数
     */
    private int doDelete(List<String> keys) {
        return sqLite.delete(keys);
    }

    /**
     * 线程池销毁
     */
    public void destroy() {
        executorService.shutdownNow();
    }
}
