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

package qunar.tc.bistoury.agent.common.cpujstack;

import com.google.common.base.Strings;

/**
 * @author 肖哥弹架构
 * @date 2022-09-12
 * @desc CPU JSTACK  KV 工具类
 */
public class KvUtils {
    /**
     * JSTACK 前缀
     */
    public static final String CPU_JSTACK_PREFIX = "cj-";

    /**
     * 获取线程名称
     * @param timestamp 线程池
     * @return 线程名
     */
    public static String getThreadNumKey(String timestamp) {
        return CPU_JSTACK_PREFIX + timestamp + "-threadNum";
    }

    /**
     * 获取CPU线程每秒总消耗或瞬时消耗名称(不带线程编号)
     * @param timestamp 时间戳
     * @return
     */
    public static String getThreadMomentCpuTimeKey(String timestamp) {
        return getThreadMomentCpuTimeKey(timestamp, null);
    }

    /**
     * 获取CPU线程每秒总消耗或瞬时消耗名称
     * @param timestamp 时间戳
     * @param threadId 线程编号
     * @return 线程工作名称
     */
    public static String getThreadMomentCpuTimeKey(String timestamp, String threadId) {
        if (Strings.isNullOrEmpty(threadId)) {
            return CPU_JSTACK_PREFIX + timestamp + "-totalCpuTime";
        } else {
            return CPU_JSTACK_PREFIX + timestamp + "-cputime-" + threadId;
        }
    }

    /**
     * 获取CPU线程总消耗或瞬时消耗名称(不带线程编号)
     * @param timestamp 时间戳
     * @return 线程工作名称
     */
    public static String getThreadMinuteCpuTimeKey(String timestamp) {
        return getThreadMinuteCpuTimeKey(timestamp, null);
    }

    /**
     * 获取CPU线程每分钟总消耗或瞬时消耗名称
     * @param timestamp 时间戳
     * @param threadId 线程编号
     * @return 线程工作名称
     */
    public static String getThreadMinuteCpuTimeKey(String timestamp, String threadId) {
        if (Strings.isNullOrEmpty(threadId)) {
            return CPU_JSTACK_PREFIX + timestamp + "-totalMinuteCpuTime";
        } else {
            return CPU_JSTACK_PREFIX + timestamp + "-minuteCpuTime-" + threadId;
        }
    }

    /**
     * 获取Jstack结果值名称
     * @param timestamp 时间戳
     * @return 结果名称
     */
    public static String getJStackResultKey(String timestamp) {
        return CPU_JSTACK_PREFIX + timestamp + "-jstack";
    }

    /**
     * 获取线程信息名称
     * @param timestamp 时间戳
     * @return 线程信息名称
     */
    public static String getThreadInfoKey(String timestamp) {
        return CPU_JSTACK_PREFIX + timestamp + "-threadinfo";
    }

    /**
     *  获取成功收集名称
     * @param timestamp 时间戳
     * @return 成功获取名称
     */
    public static String getCollectSuccessKey(String timestamp) {
        return CPU_JSTACK_PREFIX + timestamp + "-success";
    }
}
