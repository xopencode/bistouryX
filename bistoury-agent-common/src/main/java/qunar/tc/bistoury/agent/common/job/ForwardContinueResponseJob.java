package qunar.tc.bistoury.agent.common.job;

import com.google.common.util.concurrent.ListeningExecutorService;

/**
 * @author 肖哥弹架构
 * @date 2022-09-12
 * @desc 抽象重定向响应任务处理
 */
public abstract class ForwardContinueResponseJob implements ContinueResponseJob {

    protected ForwardContinueResponseJob(){}

    /**
     * 获取装饰对象
     * @return ContinueResponseJob
     */
    protected abstract ContinueResponseJob delegate();

    /**
     * 获取任务编号
     * @return
     */
    @Override
    public String getId() {
        return delegate().getId();
    }

    /**
     * 任务初始化
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        delegate().init();
    }

    /**
     * 任务响应结果处理
     * @return 响应处理结果状态
     * @throws Exception
     */
    @Override
    public boolean doResponse() throws Exception {
        return delegate().doResponse();
    }

    /**
     * 任务清理
     */
    @Override
    public void clear() {
        delegate().clear();
    }

    /**
     * 任务结束
     * @throws Exception
     */
    @Override
    public void finish() throws Exception {
        delegate().finish();
    }

    /**
     * 任务错误处理
     * @param t 异常
     */
    @Override
    public void error(Throwable t) {
        delegate().error(t);
    }

    /**
     * 任务取消
     */
    @Override
    public void cancel() {
        delegate().cancel();
    }

    /**
     * 获取任务执行监听器线程池
     * @return
     */
    @Override
    public ListeningExecutorService getExecutor() {
        return delegate().getExecutor();
    }
}
