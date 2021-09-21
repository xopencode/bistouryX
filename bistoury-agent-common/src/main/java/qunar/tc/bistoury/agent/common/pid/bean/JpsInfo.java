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

/**
 * @author 肖哥弹架构
 * @date 2022-09-12
 * @desc JAVA 进程服务信息实体
 */
public class JpsInfo {
    /**
     * JAVA 进程编号
     */
    private int pid;
    /**
     * JAVA 启动类
     */
    private String clazz;

    public JpsInfo(int pid, String clazz) {
        this.clazz = clazz;
        this.pid = pid;
    }

    public String getClazz() {
        return clazz;
    }

    public int getPid() {
        return pid;
    }
}
