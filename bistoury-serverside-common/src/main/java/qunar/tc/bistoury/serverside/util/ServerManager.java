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

package qunar.tc.bistoury.serverside.util;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qunar.tc.bistoury.common.Throwables;
import qunar.tc.bistoury.remoting.util.LocalHost;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.Query;
import java.lang.management.ManagementFactory;
import java.util.Set;

/**
 * @author 肖哥弹架构
 * @date 2021/09/06
 * @describe 服务管理,主要用于服务端进程编号、端口获取
 */
public class ServerManager {
    private static final Logger logger = LoggerFactory.getLogger(ServerManager.class);

    /**
     * 获取进程编号
     * @return 进程编号
     */
    public static int getPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return Integer.valueOf(name.substring(0, name.indexOf(64)));
    }

    /**
     * 获取Tomcat服务端口
     * @return 服务端口
     */
    public static int getTomcatPort() {
        return Integer.valueOf(getTomcatPortBySystemProperty());
    }

    /**
     * 通过JVM环境配置获取Tomcat端口
     * @return 服务端口
     */
    private static String getTomcatPortBySystemProperty() {
        String port = System.getProperty("bistoury.tomcat.port");
        if (Strings.isNullOrEmpty(port)) {
            port = getTomcatPortByMxBean();
        }
        return port;
    }

    /**
     * 通过JMX方式获取Tomcat端口
     * @return Tomcat端口
     */
    private static String getTomcatPortByMxBean() {
        String tomcatPort = "-1";
        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            if (server != null) {
                Set<ObjectName> objectNames = server.queryNames(new ObjectName("*:type=Connector,*"),
                        Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
                tomcatPort = objectNames.iterator().next().getKeyProperty("port");
            }

        } catch (Exception e) {
            logger.error("get tomcat port error", e);
            throw Throwables.propagate(e);
        }
        return tomcatPort;
    }
}
