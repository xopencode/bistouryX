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

package qunar.tc.bistoury.agent.common.pid.bean;

import java.util.Arrays;

/**
 * @author 肖哥弹架构
 * @date 2022-09-12
 * @desc 进程信息实体
 */
public class PsInfo {
    /**
     * 权限角色
     */
    private String user;
    /**
     * 进程编号
     */
    private int pid;
    /**
     * 进程启动执行命令
     */
    private String command;
    /**
     * 启动参数
     */
    private String[] params;

    public PsInfo(String user, int pid, String command, String[] params) {
        this.user = user;
        this.pid = pid;
        this.command = command;
        this.params = params;
    }

    public String getUser() {
        return user;
    }

    public int getPid() {
        return pid;
    }

    public String getCommand() {
        return command;
    }

    public String[] getParams() {
        return params;
    }

    @Override
    public String toString() {
        return "PsInfo{" +
                "user='" + user + '\'' +
                ", pid=" + pid +
                ", command='" + command + '\'' +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}
