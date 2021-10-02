package qunar.tc.bistoury.agent.common.job;

/**
 * @author 肖哥弹架构
 * @date 2022-09-12
 * @desc 响应结果任务管理策略
 */
public interface ResponseJobStore {
    /**
     * 设置可写操作
     * @param writable 可写
     */
    void setWritable(boolean writable);

    /**
     * 提交持续响应定时任务
     * @param job
     */
    void submit(ContinueResponseJob job);

    /**
     * 解析
     * @param id 任务编号
     */
    void pause(String id);

    /**
     * 重启
     * @param id 任务编号
     */
    void resume(String id);

    /**
     * 停止
     * @param id 任务编号
     */
    void stop(String id);

    /**
     * 关闭
     */
    void close();
}