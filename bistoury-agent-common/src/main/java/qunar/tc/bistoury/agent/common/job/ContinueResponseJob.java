package qunar.tc.bistoury.agent.common.job;

import com.google.common.util.concurrent.ListeningExecutorService;

/**
 * @author 肖哥弹架构
 * @date 2022-09-12
 * @desc 持续响应结果任务处理策略
 */
public interface ContinueResponseJob {
    /**
     * 编号
     * @return
     */
    String getId();

    /**
     * 初始化
     * @throws Exception
     */
    void init() throws Exception;

    /**
     * 执行响应处理
     * @return 执行结果状态
     * @throws Exception
     */
    boolean doResponse() throws Exception;

    /**
     * 执行清理
     */
    void clear();

    /**
     * 结束处理
     * @throws Exception
     */
    void finish() throws Exception;

    /**
     * 异常处理
     * @param t
     */
    void error(Throwable t);

    /**
     * 取消处理
     */
    void cancel();

    /**
     * 获取监听器任务执行线程池
     * @return
     */
    ListeningExecutorService getExecutor();
}