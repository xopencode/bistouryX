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

import com.google.common.util.concurrent.RateLimiter;

import java.io.InputStream;
import java.util.Arrays;

/**
 * @author 肖哥弹架构
 * @date 2022-09-11
 * @desc 通用进程流读取操作
 */
public class NormalProcess extends ClosableProcess {
    /**
     * 缓存区大小
     */
    private static final int BUF_SIZE = 4 * 1024;
    /**
     * 空字节常量
     */
    private static final byte[] EMPTY_BYTES = new byte[0];
    /**
     * 限流组件
     */
    private final RateLimiter rateLimiter = RateLimiter.create(16); //限制每秒read的次数
    /**
     * 缓存区
     */
    private final byte[] buffer = new byte[BUF_SIZE];

    /**
     * @param delegate 被装饰的进程
     */
    NormalProcess(Process delegate) {
        super(delegate);
    }

    /**
     * 流读取策略，每次读取一个Buffer空间的数据，如果没有则返回为空
     * @return 流内容
     * @throws Exception
     */
    @Override
    public byte[] read() throws Exception {
        rateLimiter.acquire();
        InputStream inputStream = getInputStream();
        int count = inputStream.read(buffer);
        if (count > 0) {
            return Arrays.copyOfRange(buffer, 0, count);
        } else if (count == 0) {
            return EMPTY_BYTES;
        } else {
            return null;
        }
    }
}