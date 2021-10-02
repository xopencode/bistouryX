package qunar.tc.bistoury.agent.common.job;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author 肖哥弹架构
 * @date 2022-09-16
 * @desc 默认任务响应结果处理存储
 */
public class DefaultResponseJobStore implements ResponseJobStore {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(DefaultResponseJobStore.class);
    /**
     * 任务存储《任务编号，暂停任务实例》
     */
    private final ConcurrentMap<String, PausedJob> jobs = Maps.newConcurrentMap();
    /**
     * 暂停的任务编号列表
     */
    private final Set<String> pausedJobs = Sets.newConcurrentHashSet();
    /**
     * 默认任务可处理响应结果处理
     */
    private volatile boolean writable = true;
    /**
     * 锁机制
     */
    private volatile CountDownLatch latch = new CountDownLatch(0);
    /**
     * 默认任务存储未关闭
     */
    private boolean isClosed = false;

    /**
     * 提交任务并暂停存储
     * @param job
     */
    @Override
    public void submit(ContinueResponseJob job) {
        PausedJob old;//老任务
        PausedJob pausedJob;//暂停任务

        synchronized (this) {
            if (isClosed) {
                job.error(new IllegalStateException("job store closed"));
                return;
            }
            //将任务暂停封装
            pausedJob = new PausedJob(job);
            //临时存储缓存中
            old = jobs.putIfAbsent(pausedJob.getId(), pausedJob);
        }
        //任务为第一次存储则先开始
        if (old == null) {
            logger.info("submit job {}", pausedJob.getId());
            pausedJob.start();//暂停
        }
    }

    /**
     * 暂停某任务
     * @param id 任务编号
     */
    @Override
    public void pause(String id) {
        logger.info("try pause job {}", id);
        PausedJob pausedJob = jobs.get(id);
        if (pausedJob != null) {
            pausedJob.paused();
        }
    }

    /**
     * 重启某任务
     * @param id 任务编号
     */
    @Override
    public void resume(String id) {
        logger.info("try resume job {}", id);
        PausedJob pausedJob = jobs.get(id);
        if (pausedJob != null) {
            pausedJob.resume();
        }
    }

    /**
     * 停止某任务
     * @param id 任务编号
     */
    @Override
    public void stop(String id) {
        logger.info("try stop job {}", id);
        PausedJob pausedJob = jobs.get(id);
        if (pausedJob != null) {
            pausedJob.stop();
        }
    }

    /**
     * 关闭某任务
     */
    @Override
    public void close() {
        synchronized (this) {
            if (isClosed) {
                return;
            }

            logger.info("close job store");
            //关闭某任务则不能执行写操作
            setWritable(false);
            //标记服务已结束
            isClosed = true;
        }
        //循环所有的任务执行结束操作
        for (PausedJob pausedJob : jobs.values()) {
            pausedJob.stop();
        }
    }

    /**
     * 设置某任务可写
     * @param writable 可写标记
     */
    @Override
    public synchronized void setWritable(boolean writable) {
        //存储已经关闭或者已经可写
        if (isClosed || this.writable == writable) {
            return;
        }

        logger.info("change writable to {}", writable);
        this.writable = writable;//设置可写标记
        if (writable) {
            latch.countDown();//释放锁
        } else {
            latch = new CountDownLatch(1);//加锁
        }
    }

    /**
     * 暂停任务类
     */
    private class PausedJob {
        /**
         * 持续响应结果任务
         */
        private final ContinueResponseJob job;
        /**
         * 执行服务监线程池
         */
        private final ListeningExecutorService executor;
        /**
         * 默认标记任务暂停
         */
        private boolean paused = false;
        /**
         * 默认标记任务停止
         */
        private boolean stopped = false;
        /**
         * 监听者异步对象
         */
        private ListenableFuture<?> finishFuture;

        private PausedJob(ContinueResponseJob job) {
            this.job = new WrappedJob(job);
            this.executor = job.getExecutor();
        }

        /**
         * @return 任务编号
         */
        public String getId() {
            return job.getId();
        }

        /**
         * 任务初始化
         * @throws Exception
         */
        public void init() throws Exception {
            job.init();
        }

        /**
         * 任务执行
         */
        public synchronized void start() {
            if (stopped) {
                return;
            }
            //执行任务并返回异步结果对象
            this.finishFuture = executor.submit(new JobRunner(this));
        }

        /**
         * 任务暂停
         */
        public synchronized void paused() {
            if (!this.paused) {
                logger.debug("paused job {}", getId());
                this.paused = true;
            }
        }

        /**
         * 执行任务并相应结果
         * @return 执行结果状态
         * @throws Exception
         */
        public boolean doResponse() throws Exception {
            return job.doResponse();
        }

        /**
         * 停止任务重启
         */
        public synchronized void resume() {
            if (stopped || !this.paused) {
                return;
            }

            this.paused = false;
            boolean removed = pausedJobs.remove(getId());
            logger.debug("resume job {}, {}", removed, getId());
            if (removed) {
                this.finishFuture = executor.submit(new JobRunner(this));
            }
        }

        /**
         * 停止任务
         */
        public void stop() {
            synchronized (PausedJob.this) {
                if (stopped) {
                    return;
                }

                logger.debug("stop job {}", getId());
                stopped = true;
                if (finishFuture != null) {
                    finishFuture.cancel(true);
                }
                removeFromStore();
            }
            job.cancel();
        }

        /**
         * 结束任务
         * @throws Exception
         */
        public void finish() throws Exception {
            synchronized (PausedJob.this) {
                if (stopped) {
                    return;
                }
                logger.debug("finish job {}", getId());
                removeFromStore();
            }
            job.finish();
        }

        /**
         * 任务错误处理
         * @param t
         */
        public void error(Throwable t) {
            synchronized (PausedJob.this) {
                if (stopped) {
                    return;
                }

                logger.debug("error job {}", getId(), t);
                removeFromStore();
            }
            job.error(t);
        }

        /**
         * 任务是否停止
         * @return
         */
        public synchronized boolean isStopped() {
            return stopped;
        }

        /**
         * 暂停任务
         * @return
         */
        public synchronized boolean doPausedIfNeed() {
            if (stopped) {
                return true;
            }

            if (paused) {
                logger.debug("do pause job {}", getId());
                pausedJobs.add(getId());
                finishFuture = null;
                return true;
            }
            return false;
        }

        /**
         * 删除缓存任务
         */
        private void removeFromStore() {
            pausedJobs.remove(getId());
            jobs.remove(getId());
        }
    }

    /**
     * 任务执行
     */
    private class JobRunner implements Runnable {
        /**
         * 暂停任务
         */
        private final PausedJob job;

        private JobRunner(PausedJob job) {
            this.job = job;
        }

        /**
         * 运行暂停任务,模板方法
         */
        @Override
        public void run() {
            logger.debug("run job {}", job.getId());
            try {
                if (job.isStopped()) {
                    return;
                }

                job.init();
                doRun();
            } catch (Throwable t) {
                job.error(t);
            }
        }

        /**
         * 执行任务
         * @throws Exception
         */
        private void doRun() throws Exception {
            while (true) {
                latch.await();
                if (job.isStopped() || job.doPausedIfNeed()) {
                    logger.debug("stop or paused job {}", job.getId());
                    return;
                }

                boolean end = job.doResponse();
                if (end) {
                    job.finish();
                    return;
                }
            }
        }
    }

    /**
     * 装饰者JOB任务
     */
    private static class WrappedJob extends ForwardContinueResponseJob {
        /**
         * 持续响应结果任务
         */
        private final ContinueResponseJob delegate;
        /**
         * 默认未初始化
         */
        private boolean init = false;
        /**
         * 默认未清理
         */
        private boolean clear = false;

        public WrappedJob(ContinueResponseJob delegate) {
            this.delegate = delegate;
        }

        /**
         *
         * @return
         */
        @Override
        protected ContinueResponseJob delegate() {
            return delegate;
        }

        /**
         *
         * @throws Exception
         */
        @Override
        public synchronized void init() throws Exception {
            if (!init && !clear) {
                init = true;
                logger.debug("job init {}", getId());
                super.init();
            }
        }

        /**
         *
         */
        @Override
        public synchronized void clear() {
            if (!clear) {
                clear = true;
                logger.debug("job clear {}", getId());
                try {
                    super.clear();
                } catch (Exception e) {
                    logger.error("job clear error, {}", getId());
                }
            }
        }

        /**
         * 任务结束处理
         * @throws Exception
         */
        @Override
        public void finish() throws Exception {
            logger.debug("job finish {}", delegate.getId());
            clear();
            super.finish();
        }

        /**
         * 任务异常处理
         * @param t 异常
         */
        @Override
        public void error(Throwable t) {
            logger.debug("job error {}", delegate.getId(), t);
            clear();
            super.error(t);
        }

        /**
         * 取消任务
         */
        @Override
        public void cancel() {
            logger.debug("job cancel {}", delegate.getId());
            clear();
            super.cancel();
        }
    }

    @Override
    public String toString() {
        return "DefaultResponseJobStore{" +
                "jobs=" + jobs +
                ", pausedJobs=" + pausedJobs +
                ", writable=" + writable +
                ", latch=" + latch +
                ", isClosed=" + isClosed +
                '}';
    }
}
