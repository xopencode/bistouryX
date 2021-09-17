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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 肖哥弹架构
 * @date 2022-09-10
 * @desc ZK客户端缓存管理
 */
public class ZKClientCache {
    private static final Logger logger = LoggerFactory.getLogger(ZKClientCache.class);
    /**
     * ZK客户端缓存容器
     */
    private static final Map<String, ZKClient> CACHE = new HashMap<>();
    /**
     * 本地ZK配置文件
     */
    private static final String LOCAL_ZK_TAG_FILE = "/tmp/bistoury/proxy.conf";

    /**
     * 根据ZK地址获取ZK客户端
     * @param address ZK服务器地址
     * @return ZK客户端
     */
    public synchronized static ZKClient get(String address) {

        logger.info("get zkclient for {}", address);
        ZKClient client = CACHE.get(address);
        if (client == null) {
            client = getZkClient(address);
            CACHE.put(address, client);
        } else {
            client = CACHE.get(address);
        }
        client.incrementReference();
        return client;
    }

    /**
     * 获取ZK客户端
     * @param address ZK地址
     * @return ZK客户端
     */
    private static ZKClient getZkClient(final String address) {
        if (isLocal()) {
            //如果/tmp/bistoury/proxy.conf文件存在,则为MockZK
            return new MockZkClientImpl(LOCAL_ZK_TAG_FILE);
        }
        //创建ZK客户端
        return new ZKClientImpl(address);
    }

    /**
     * 判断是否为本地配置文件
     * @return
     */
    private static boolean isLocal() {
        File file = new File(LOCAL_ZK_TAG_FILE);
        return file.exists() && file.isFile();
    }

}
