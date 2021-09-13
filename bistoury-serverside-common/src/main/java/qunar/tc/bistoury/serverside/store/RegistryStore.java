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

package qunar.tc.bistoury.serverside.store;

import qunar.tc.bistoury.serverside.configuration.DynamicConfigLoader;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author 肖哥弹架构
 * @date 2022-09-10
 * @desc 注册中心信息管理
 */
public class RegistryStore {

    /**
     * 默认ZK注册中心地址
     */
    private static final String DEFAULT_ZK = "default";
    /**
     * 注册中心配置属性文件
     */
    private static final String REGISTRY_CONFIG = "registry.properties";
    /**
     * 代理基础地址
     */
    private String newBaseRoot = "/bistoury/proxy/new/group/";
    /**
     * 注册中心地址
     */
    private String zkAddress;
    /**
     * Proxy UI地址
     */
    private String pathForNewUi;

    /**
     * 初始化(获取UI与ZK地址)
     */
    @PostConstruct
    public void init() {
        Map<String, String> registries = DynamicConfigLoader.load(REGISTRY_CONFIG).asMap();
        zkAddress = registries.get(DEFAULT_ZK);
        pathForNewUi = newBaseRoot + "ui";
    }


    public String getZkAddress() {
        return zkAddress;
    }

    public String getProxyZkPathForNewUi() {
        return pathForNewUi;
    }
}
