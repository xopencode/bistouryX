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

package qunar.tc.bistoury.serverside.common;

import org.apache.curator.framework.state.ConnectionStateListener;

import java.util.List;

/**
 * @author 肖哥弹架构
 * @date 2022-09-10
 * @desc ZK客户端
 */
public interface ZKClient {
    /**
     * 删除服务地址
     * @param path 服务地址
     * @throws Exception
     */
    void deletePath(String path) throws Exception;

    /**
     * 获取服务提供者地址列表
     * @param path 服务地址
     * @return 提供者地址列表
     * @throws Exception
     */
    List<String> getChildren(String path) throws Exception;

    /**
     *  判断服务地址是否存在
     * @param path 服务地址
     * @return 是否存在服务提供者
     */
    boolean checkExist(String path);

    /**
     * 添加持久服务提供者地址
     * @param path 服务提供者地址
     * @throws Exception
     */
    void addPersistentNode(String path) throws Exception;

    /**
     *  添加临时服务提供者地址
     * @param path
     * @return
     * @throws Exception
     */
    String addEphemeralNode(String path) throws Exception;

    /**
     * 添加链接状态监听器
     * @param listener
     */
    void addConnectionChangeListener(ConnectionStateListener listener);

    /**
     * 引用数增加
     */
    void incrementReference();

    /**
     * 关闭客户端
     */
    void close();
}
