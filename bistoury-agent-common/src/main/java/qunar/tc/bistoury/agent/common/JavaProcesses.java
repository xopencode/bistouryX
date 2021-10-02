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

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author 肖哥弹架构
 * @date 2022-09-11
 * @desc Java process管理者
 */
public class JavaProcesses {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(JavaProcesses.class);
    /**
     * 进程索引编号
     */
    private static final AtomicLong index = new AtomicLong(0);
    /**
     * 是否关闭进程
     */
    private static boolean shutdown = false;
    /**
     * 进程管理容器《进程索引编号，JAVA进程》
     */
    private static final Map<Long, Process> processes = Maps.newConcurrentMap();

    /**
     * 静态块，添加进程关闭信号处理
     */
    static {
        /**
         * 关闭bistoury进程后，关闭所有管理进程
         */
        Runtime.getRuntime().addShutdownHook(new Thread("process-shutdown-clear") {
            @Override
            public void run() {
                JavaProcesses.clear();
            }
        });
    }
    /**
     * 将进程注册到{processes}容器中
     * @param process 进程
     * @return 进程存储索引编号
     */
    public static long register(Process process) {
        long i = index.getAndIncrement();
        logger.debug("register java process: {}", i);
        synchronized (JavaProcesses.class) {
            if (!shutdown) {
                processes.put(i, process);
            } else {
                process.destroy();
                throw new IllegalStateException("system already shutdown");
            }
        }
        return i;
    }

    /**
     * 从{processes}容器中删除对应的进程
     * @param index 进程编号
     */
    public static void remove(long index) {
        logger.debug("remove java process: {}", index);
        processes.remove(index);
    }

    /**
     * 关闭管理的所有进程
     */
    public static void clear() {
        synchronized (JavaProcesses.class) {
            shutdown = true;
        }
        int count = 0;
        for (Map.Entry<Long, Process> entry : processes.entrySet()) {
            entry.getValue().destroy();
            logger.debug("clear java process: {}", entry.getKey());
            ++count;
        }
        logger.info("clear java process count: " + count);
    }
}