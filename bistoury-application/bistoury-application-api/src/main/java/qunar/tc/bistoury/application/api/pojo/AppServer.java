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

package qunar.tc.bistoury.application.api.pojo;

/**
 * @author 肖哥弹架构
 * @update 2021/09/06
 * @describe 应用服务实体类
 */
public class AppServer {
    /**
     * 服务编号
     */
    private String serverId;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 服务端口
     */
    private int port;
    /**
     * 服务主机地址
     */
    private String host;
    /**
     * 服务日志目录
     */
    private String logDir;
    /**
     * 服务所属房间
     */
    private String room;
    /**
     * 应用编号
     */
    private String appCode;
    /**
     * 服务是否自动Jstack,默认为false
     */
    private boolean autoJStackEnable = false;
    /**
     * 服务是否自动Jmap直方图,默认为false
     */
    private boolean autoJMapHistoEnable = false;

    public AppServer() {

    }

    public AppServer(String serverId, String ip, int port, String host, String logDir, String room, String appCode) {
        this.serverId = serverId;
        this.ip = ip;
        this.port = port;
        this.host = host;
        this.logDir = logDir;
        this.room = room;
        this.appCode = appCode;
    }

    public AppServer(String serverId, String ip, int port, String host, String logDir, String room, String appCode, boolean autoJStackEnable, boolean autoJMapHistoEnable) {
        this.serverId = serverId;
        this.ip = ip;
        this.port = port;
        this.host = host;
        this.logDir = logDir;
        this.room = room;
        this.appCode = appCode;
        this.autoJStackEnable = autoJStackEnable;
        this.autoJMapHistoEnable = autoJMapHistoEnable;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getLogDir() {
        return logDir;
    }

    public void setLogDir(String logDir) {
        this.logDir = logDir;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public boolean isAutoJStackEnable() {
        return autoJStackEnable;
    }

    public void setAutoJStackEnable(boolean autoJStackEnable) {
        this.autoJStackEnable = autoJStackEnable;
    }

    public boolean isAutoJMapHistoEnable() {
        return autoJMapHistoEnable;
    }

    public void setAutoJMapHistoEnable(boolean autoJMapHistoEnable) {
        this.autoJMapHistoEnable = autoJMapHistoEnable;
    }

    @Override
    public String toString() {
        return "AppServer{" +
                "serverId='" + serverId + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", host='" + host + '\'' +
                ", logDir='" + logDir + '\'' +
                ", room='" + room + '\'' +
                ", appCode='" + appCode + '\'' +
                ", autoJStackEnable=" + autoJStackEnable +
                ", autoJMapHistoEnable=" + autoJMapHistoEnable +
                '}';
    }
}
