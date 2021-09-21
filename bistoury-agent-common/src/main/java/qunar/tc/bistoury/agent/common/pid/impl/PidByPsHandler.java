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

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qunar.tc.bistoury.agent.common.ClosableProcess;
import qunar.tc.bistoury.agent.common.ClosableProcesses;
import qunar.tc.bistoury.agent.common.pid.PidHandler;
import qunar.tc.bistoury.agent.common.pid.bean.PsInfo;
import qunar.tc.bistoury.clientside.common.meta.MetaStore;
import qunar.tc.bistoury.clientside.common.meta.MetaStores;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author 肖哥弹架构
 * @date 2022-09-14
 * @desc 获取Tomcat启动的进程PID，且通过PS获取PID策略
 */
public class PidByPsHandler extends AbstractPidHandler implements PidHandler {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(PidByPsHandler.class);
    /**
     * 获取Agent元数据对象
     */
    private static final MetaStore META_STORE = MetaStores.getMetaStore();
    /**
     * 从Agent元数据中获取执行tomcat linxu角色,默认{tomcat}
     */
    private static final String TOMCAT_USER = META_STORE.getStringProperty("tomcat.user", "tomcat");
    /**
     * tomcat 容器执行命令,默认为{/home/java/default/bin/java}
     */
    private static final String TOMCAT_COMMAND = META_STORE.getStringProperty("tomcat.command", "/home/java/default/bin/java");
    /**
     * 用户索引位
     */
    private static final int USER_INDEX = 0;
    /**
     *  PID索引位
     */
    private static final int PID_INDEX = 1;
    /**
     * 执行命令索引位
     */
    private static final int COMMAND_INDEX = 10;

    /**
     * PS命令执行优先级
     * @return 优先级
     */
    @Override
    public int priority() {
        return Priority.FROM_PS_PRIORITY;
    }

    /**
     *  获取Tomcat进程信息，并执行PS获取Pid流程
     * @return PID
     */
    @Override
    protected int doGetPid() {
        String psInfo = getPsInfo();
        if (!Strings.isNullOrEmpty(psInfo)) {
            ArrayListMultimap<String, PsInfo> multimap = parsePsInfo(psInfo);
            List<PsInfo> infos = multimap.get(TOMCAT_COMMAND);//获取tomcat开启的进程信息
            if (infos != null && infos.size() > 0) {
                for (PsInfo info : infos) {
                    if (TOMCAT_USER.equalsIgnoreCase(info.getUser())) {//获取指定Tomcat_User用户下的进程
                        return info.getPid();
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 获取所有JAVA 进程命令
     * 例如：[27152 org.jetbrains.jps.cmdline.Launcher, 30916 qunar.tc.bistoury.agent.common.pid.Jps, 13672 ]
     * @param psInfo {ps aux | grep java}命令之后信息
     * @return JAVA进程信息
     */
    private static ArrayListMultimap<String, PsInfo> parsePsInfo(final String psInfo) {
        ArrayListMultimap<String, PsInfo> multimap = ArrayListMultimap.create();
        String all = psInfo.replaceAll("[( )\t]+", " ");
        String[] lines = all.split("[\n\r(\r\n)]");
        for (String line : lines) {
            if (Strings.isNullOrEmpty(line)) {
                continue;
            }
            String[] pieces = line.split(" ");
            final String user = pieces[USER_INDEX];//获取执行用户名
            final int pid = Integer.parseInt(pieces[PID_INDEX]);//获取PID值
            final String command = pieces[COMMAND_INDEX];//获取执行JAVA Main命令
            final String[] params = Arrays.copyOfRange(pieces, COMMAND_INDEX + 1, pieces.length);
            PsInfo process = new PsInfo(user, pid, command, params);
            multimap.put(command, process);
        }
        return multimap;
    }

    /**
     * 获取执行PS命令，并过滤JAVA相关返回值 {ps aux | grep java}
     * @return java进程信息值
     */
    private static String getPsInfo() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ClosableProcess process = ClosableProcesses.wrap(new ProcessBuilder("/bin/sh", "-c", "ps aux | grep java").redirectErrorStream(true).start());
             InputStream inputStream = process.getInputStream()) {
            ByteStreams.copy(inputStream, outputStream);
            return outputStream.toString("utf8");
        } catch (Exception e) {
            logger.error("execute ps aux|grep java error", e);
            return null;
        }
    }
}
