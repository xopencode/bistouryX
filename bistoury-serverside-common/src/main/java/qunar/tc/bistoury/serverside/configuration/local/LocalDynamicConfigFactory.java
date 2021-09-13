
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

package qunar.tc.bistoury.serverside.configuration.local;

import qunar.tc.bistoury.serverside.configuration.DynamicConfig;
import qunar.tc.bistoury.serverside.configuration.DynamicConfigFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author 肖哥弹架构
 * @date 2022-09-10
 * @desc 本地文件动态配置工厂
 */
public class LocalDynamicConfigFactory implements DynamicConfigFactory<LocalDynamicConfig> {
    /**
     * 配置修改监控对象
     */
    private final ConfigWatcher watcher = new ConfigWatcher();
    /**
     * 配置容器<配置文件名,配置实体对象>
     */
    private final ConcurrentMap<String, LocalDynamicConfig> configs = new ConcurrentHashMap<>();

    /**
     * 创建动态配置对象
     * @param name 动态配置名
     * @param failOnNotExist 配置文件不存在则抛异常
     * @return
     */
    @Override
    public DynamicConfig<LocalDynamicConfig> create(final String name, final boolean failOnNotExist) {
        if (configs.containsKey(name)) {
            return configs.get(name);
        }
        //不存在则创建动态配置对象
        return doCreate(name, failOnNotExist);
    }

    /**
     * 创建本地文件动态配置对象
     * @param name 配置文件名
     * @param failOnNotExist 配置文件不存在则抛异常
     * @return 本地动态配置对象
     */
    private LocalDynamicConfig doCreate(final String name, final boolean failOnNotExist) {
        final LocalDynamicConfig prev = configs.putIfAbsent(name, new LocalDynamicConfig(name, failOnNotExist));
        final LocalDynamicConfig config = configs.get(name);
        //新配置对象
        if (prev == null) {
            //添加入监控对象中
            watcher.addWatch(config);
            //执行配置加载与触发监听器
            config.onConfigModified();
        }
        return config;
    }
}
