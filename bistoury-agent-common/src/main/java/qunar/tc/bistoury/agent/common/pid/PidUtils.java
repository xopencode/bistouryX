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

package qunar.tc.bistoury.agent.common.pid;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qunar.tc.bistoury.agent.common.pid.impl.PidByJpsHandler;
import qunar.tc.bistoury.agent.common.pid.impl.PidByPsHandler;
import qunar.tc.bistoury.agent.common.pid.impl.PidBySystemPropertyHandler;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author 肖哥弹架构
 * @date 2022-09-12
 * @desc PID工具类
 */
public class PidUtils {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(PidUtils.class);
    /**
     * PID处理策略实例容器
     */
    private static final List<PidHandler> PID_HANDLERS = initPidHandler();

    /**
     *  初始化所有的PidHandler实现类
     * @return 所有Pidhandler实例
     */
    private static List<PidHandler> initPidHandler() {
        List<PidHandler> handlers = Lists.newArrayList();
        //默认pid处理策略
        handlers.add(new PidBySystemPropertyHandler());
        //是否开启bistoury.pid.handler.jps.enable
        if (Boolean.parseBoolean(System.getProperty("bistoury.pid.handler.jps.enable", "true"))) {
            handlers.add(new PidByJpsHandler());
        }
        //是否开启bistoury.pid.handler.ps.enable
        if (Boolean.parseBoolean(System.getProperty("bistoury.pid.handler.ps.enable", "true"))) {
            handlers.add(new PidByPsHandler());
        }
        //通过插件获取所有的PidHandlerFactory实现类
        ServiceLoader<PidHandlerFactory> handlerFactories = ServiceLoader.load(PidHandlerFactory.class);
        //循环所有的工厂,创建PidHandler实例
        for (PidHandlerFactory factory : handlerFactories) {
            handlers.add(factory.create());
        }
        //工具优先级排序
        Collections.sort(handlers, new Comparator<PidHandler>() {
            @Override
            public int compare(PidHandler o1, PidHandler o2) {
                return Integer.compare(o1.priority(), o2.priority());
            }
        });
        return ImmutableList.copyOf(handlers);
    }

    /**
     * @return 获取进程编号,获取不到则为-1
     */
    public static int getPid() {
        //循环所有的策略直到获取到进程编号
        for (PidHandler handler : PID_HANDLERS) {
            int pid = handler.getPid();
            if (pid > 0) {
                logger.info("get pid by {} success, pid is {}", handler.getClass().getSimpleName(), pid);
                return pid;
            }
        }
        return -1;
    }
}