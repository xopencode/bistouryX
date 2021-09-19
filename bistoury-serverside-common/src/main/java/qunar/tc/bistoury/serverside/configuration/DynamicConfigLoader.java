
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

package qunar.tc.bistoury.serverside.configuration;

import java.util.ServiceLoader;

/**
 * @author 肖哥弹架构
 * @date 2022-09-10
 * @desc 动态配置加载管理类
 */
public final class DynamicConfigLoader {
    /**
     * 单利动态配置工厂
     */
    private static final DynamicConfigFactory FACTORY;

    /**
     * 静态代码块（SPI插件机制"META-INF/services/"）
     */
    static {
        //SPI插件机制对象
        ServiceLoader<DynamicConfigFactory> factories
                = ServiceLoader.load(DynamicConfigFactory.class);
        DynamicConfigFactory instance = null;
        //获取动态配置工厂,获取第一个则退出
        for (DynamicConfigFactory factory : factories) {
            instance = factory;
            break;
        }

        FACTORY = instance;
    }

    /**
     * 根据名字创建动态配置对象
     * @param name 动态配置名
     * @param <T> 动态配置泛化实体
     * @return DynamicConfig
     */
    public static <T> DynamicConfig<T> load(final String name) {
        return load(name, true);
    }

    /**
     * 根据动态配置名加载动态配置对象
     * @param name 动态配置名
     * @param failOnNotExist 是否不存在则失败
     * @param <T> 动态配置泛化实体
     * @return DynamicConfig
     */
    @SuppressWarnings("unchecked")
    public static <T> DynamicConfig<T> load(final String name, final boolean failOnNotExist) {
        return FACTORY.create(name, failOnNotExist);
    }
}
