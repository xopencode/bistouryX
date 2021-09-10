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

package qunar.tc.bistoury.proxy.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import qunar.tc.bistoury.remoting.util.LocalHost;
import qunar.tc.bistoury.serverside.bean.ApiResult;
import qunar.tc.bistoury.serverside.configuration.DynamicConfig;
import qunar.tc.bistoury.serverside.configuration.DynamicConfigLoader;
import qunar.tc.bistoury.serverside.util.ResultHelper;

import javax.annotation.PostConstruct;

/**
 * @author leix.xie
 * @author  肖哥弹架构
 * @date 2019/5/23 16:22
 * @update 2021/9/07 18:45
 * @describe Proxy为Agent提供服务连接地址
 */
@Controller
public class ProxyConfigForAgentGetController {
    //代理配置对象
    private ProxyConfig proxyConfig;

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        //获取动态配置
        DynamicConfig dynamicConfig = DynamicConfigLoader.load("global.properties");
        proxyConfig = new ProxyConfig(LocalHost.getLocalHost(), dynamicConfig.getInt("agent.newport", -1), dynamicConfig.getInt("heartbeatSec", 30));
    }

    /**
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/proxy/config/foragent")
    public ApiResult getProxyConfig() {
        return ResultHelper.success(proxyConfig);
    }

    /**
     * 代理配置对象
     */
    private static class ProxyConfig {
        //Ip地址
        private final String ip;
        //端口
        private final int port;
        //心跳间隔
        private final int heartbeatSec;

        private ProxyConfig(String ip, int port, int heartbeatSec) {
            this.ip = ip;
            this.port = port;
            this.heartbeatSec = heartbeatSec;
        }

        public String getIp() {
            return ip;
        }

        public int getPort() {
            return port;
        }

        public int getHeartbeatSec() {
            return heartbeatSec;
        }

        @Override
        public String toString() {
            return "ProxyConfig{" +
                    "ip='" + ip + '\'' +
                    ", port=" + port +
                    ", heartbeatSec=" + heartbeatSec +
                    '}';
        }
    }
}
