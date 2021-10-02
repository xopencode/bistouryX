package qunar.tc.bistoury.agent.common.job;

import com.google.common.util.concurrent.SettableFuture;
import qunar.tc.bistoury.agent.common.ResponseHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @author 肖哥弹架构
 * @date 2022-09-12
 * @desc 抽象字节内容任务类处理
 */
public abstract class BytesJob implements ContinueResponseJob {
    /**
     * 缓存区大小
     */
    private static final int BUFFER_SIZE = 4 * 1024;
    /**
     * 任务编号
     */
    private final String id;
    /**
     * 响应处理
     */
    private final ResponseHandler handler;
    /**
     * 异步
     */
    private final SettableFuture<Integer> future;
    /**
     * 输入流
     */
    private InputStream inputStream;

    protected BytesJob(String id, ResponseHandler handler, SettableFuture<Integer> future) {
        this.id = id;
        this.handler = handler;
        this.future = future;
    }

    /**
     * 任务编号
     * @return
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * 初始化
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        //获取字节
        byte[] bytes = getBytes();
        //将字节转换成输入流
        inputStream = new ByteArrayInputStream(bytes);
    }

    /**
     * 抽象获取字节方法
     * @return
     * @throws Exception
     */
    protected abstract byte[] getBytes() throws Exception;

    /**
     * 响应处理
     * @return 处理是否成功
     * @throws Exception
     */
    @Override
    public boolean doResponse() throws Exception {
        //字节缓存区
        byte[] bytes = new byte[BUFFER_SIZE];
        //输入流
        int count = inputStream.read(bytes);
        //读取缓存已经等于4*1024
        if (count == BUFFER_SIZE) {
            //流处理
            handler.handle(bytes);
        } else if (count > 0) {
            //流处理
            handler.handle(Arrays.copyOf(bytes, count));
        }
        return count == -1;
    }

    /**
     * NOOP
     */
    @Override
    public void clear() {}

    /**
     * 标记异步已结束
     */
    @Override
    public void finish() {
        future.set(0);
    }

    /**
     * 设置异步错误
     * @param t
     */
    @Override
    public void error(Throwable t) {
        future.setException(t);
    }

    /**
     * 异步任务取消
     */
    @Override
    public void cancel() {
        future.cancel(true);
    }
}
