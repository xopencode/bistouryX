/*
 * Copyright (C) 2019 Qunar, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package qunar.tc.bistoury.agent.common;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author 肖哥弹架构
 * @date 2022-09-11
 * @desc process 关闭抽象装饰类
 */
public abstract class ClosableProcess extends Process implements Closeable {
    /**
     * JDK 进程对象
     */
    private final Process delegate;
    /**
     * 进程编号（自增长）
     */
    private final long id;

    ClosableProcess(Process delegate) {
        this.delegate = delegate;
        //获取存储后的进程分配的编号
        this.id = JavaProcesses.register(delegate);
    }

    /**
     * 输出流
     * @return
     */
    @Override
    public OutputStream getOutputStream() {
        return delegate.getOutputStream();
    }

    /**
     * 输入流
     * @return
     */
    @Override
    public InputStream getInputStream() {
        return delegate.getInputStream();
    }

    /**
     * 错误流
     * @return
     */
    @Override
    public InputStream getErrorStream() {
        return delegate.getErrorStream();
    }

    /**
     * 让当前线程处于等待状态
     * @return
     * @throws InterruptedException
     */
    @Override
    public int waitFor() throws InterruptedException {
        return delegate.waitFor();
    }

    /**
     * @return 返回子进程退出值
     */
    @Override
    public int exitValue() {
        return delegate.exitValue();
    }

    /**
     * 进程销毁
     */
    @Override
    public void destroy() {
        delegate.destroy();
        //删除销毁的进程分配编号
        JavaProcesses.remove(id);
    }

    /**
     * 抽象读取进程字节内容
     * @return 读取字节内容
     * @throws Exception
     */
    public abstract byte[] read() throws Exception;

    /**
     * 销毁进程
     */
    @Override
    public void close() {
        destroy();
    }
}
