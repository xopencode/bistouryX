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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qunar.tc.bistoury.agent.common.pid.PidHandler;

/**
 * @author 肖哥弹架构
 * @date 2022-09-14
 * @desc PID抽象类（异常处理）
 */
public abstract class AbstractPidHandler implements PidHandler {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPidHandler.class);

    /**
     * 公共方法（异常如何处理）
     * @return
     */
    @Override
    public final int getPid() {
        try {
            //指定钩子方法
            return doGetPid();
        } catch (Exception e) {
            logger.error("get pid error, {}", getClass().getName(), e);
            return -1;
        }
    }

    /**
     * 获取PID抽象方法（钩子方法）
     * @return pid
     */
    protected abstract int doGetPid();
}
