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

/**
 * @author 肖哥弹架构
 * @date 2022-09-11
 * @desc 关闭进程生产管理
 */
public class ClosableProcesses {
    /**
     * 关闭进程生产（根据OS环境判断）
     * @param process
     * @return
     */
    public static ClosableProcess wrap(Process process) {
        if (isUnixProcess(process)) {
            return new UnixProcess(process);
        } else {
            return new NormalProcess(process);
        }
    }

    /**
     * 判断是否为unix进程
     * @param process 进程
     * @return 是否为unix
     */
    private static boolean isUnixProcess(Process process) {
        try {
            //获取进程
            Class<? extends Process> clazz = process.getClass();
            //进程等于UNIXProcess
            return clazz.getName().equals("java.lang.UNIXProcess");
        } catch (Exception e) {
            return false;
        }
    }
}