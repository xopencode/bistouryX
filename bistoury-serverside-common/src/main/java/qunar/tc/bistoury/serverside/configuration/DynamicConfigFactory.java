
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

/**
 * @author 肖哥弹架构
 * @date 2022-09-10
 * @desc 动态配置工厂
 */
public interface DynamicConfigFactory<T>{
    /**
     * 创建动态配置策略
     * @param name 动态配置名
     * @param failOnNotExist 配置文件不存在则抛异常
     * @return 动态配置对象
     */
    DynamicConfig<T> create(String name, boolean failOnNotExist);
}