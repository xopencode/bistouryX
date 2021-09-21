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

package qunar.tc.bistoury.agent.common.pid.impl;

/**
 * @author 肖哥弹架构
 * @date 2022-09-14
 * @desc 优先级
 */
public class Priority {
    /**
     *  系统属性优先级
     */
    public static final int FROM_SYSTEM_PROPERTY_PRIORITY = Integer.MIN_VALUE;
    /**
     * JPS优先级
     */
    public static final int FROM_JPS_PRIORITY = 10000;
    /**
     * PS优先级
     */
    public static final int FROM_PS_PRIORITY = 20000;
}
