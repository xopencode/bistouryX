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

package qunar.tc.bistoury.agent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author zhenyu.nie created on 2018 2018/10/25 19:09
 * @author xiaoailiang update on 2021 2021/09/03 19:00
 * 单个Proxy配置类
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class ProxyConfig {
    //Proxy IP地址
    private String ip;
    //Proxy 端口
    private int port;
    //Proxy 心跳
    private int heartbeatSec;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getHeartbeatSec() {
        return heartbeatSec;
    }

    public void setHeartbeatSec(int heartbeatSec) {
        this.heartbeatSec = heartbeatSec;
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
