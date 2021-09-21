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

import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qunar.tc.bistoury.agent.common.pid.Jps;
import qunar.tc.bistoury.agent.common.pid.PidHandler;
import qunar.tc.bistoury.agent.common.pid.bean.JpsInfo;
import qunar.tc.bistoury.agent.common.pid.bean.Res;

import java.util.List;

/**
 * @author 肖哥弹架构
 * @date 2022-09-14
 * @desc 获取Tomcat启动的进程PID，且通过PS获取PID策略
 */
public class PidByJpsHandler extends AbstractPidHandler implements PidHandler {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(PidByJpsHandler.class);
    /**
     * 空格分割对象
     */
    private static final Splitter SPLITTER = Splitter.on(" ").trimResults().omitEmptyStrings();
    /**
     * PID索引位
     */
    private static final int JPS_PID_INDEX = 0;
    /**
     * JPS CLASS 索引位
     */
    private static final int JPS_CLASS_INDEX = 1;
    /**
     * JPS 标记启动类，默认Ｔomcat{org.apache.catalina.startup.Bootstrap}启动类
     */
    private static final String JPS_SYMBOL_CLASS = System.getProperty("bistoury.pid.handler.jps.symbol.class", "org.apache.catalina.startup.Bootstrap");

    /**
     * JPS优先级
     * @return 优先级
     */
    @Override
    public int priority() {
        return Priority.FROM_JPS_PRIORITY;
    }

    /**
     * 获取Tomcat启动进程PID
     * @return PID
     */
    @Override
    protected int doGetPid() {
        Res<List<String>> res = getJpsInfo();
        if (res.getCode() == 0) {
            ArrayListMultimap<String, JpsInfo> multimap = parseJpsInfo(res.getData());
            //获取{bistoury.pid.handler.jps.symbol.class}指定的类PID
            List<JpsInfo> jpsInfos = multimap.get(JPS_SYMBOL_CLASS);
            if (jpsInfos.size() > 0) {
                return jpsInfos.iterator().next().getPid();
            } else {
                return -1;
            }
        } else {
            logger.error(res.getMessage());
            return -1;
        }
    }

    /**
     * 解析JPS获取的信息
     * @param jpsInfos
     * @return
     */
    private ArrayListMultimap<String, JpsInfo> parseJpsInfo(List<String> jpsInfos) {
        //所有JAVA启动类进程信息
        ArrayListMultimap<String, JpsInfo> multimap = ArrayListMultimap.create();
        for (String jpsInfo : jpsInfos) {
            //按照空格分割
            List<String> list = SPLITTER.splitToList(jpsInfo);
            if (list.size() == 2) {
                //获取PID
                final int pid = Integer.parseInt(list.get(JPS_PID_INDEX));
                //获取启动类
                final String clazz = list.get(JPS_CLASS_INDEX);
                multimap.put(clazz, new JpsInfo(pid, clazz));
            }
        }
        return multimap;
    }

    /**
     * 通过JPS命令获取所有启动的JAVA进程信息
     * @return JAVA PID信息列表
     */
    private Res<List<String>> getJpsInfo() {
        Res<List<String>> res = new Res<>();
        Jps.executeJps(new String[]{"-l"}, res);
        return res;
    }

}
