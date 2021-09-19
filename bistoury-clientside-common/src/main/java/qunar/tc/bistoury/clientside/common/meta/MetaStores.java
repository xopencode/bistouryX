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

package qunar.tc.bistoury.clientside.common.meta;

/**
 * @author 肖哥弹架构
 * @date 2022-09-13
 * @desc 元数据存储管理类
 */
public class MetaStores {
    /**
     * 元数据存储对象
     */
    private static final MetaStore metaStore = new DefaultMetaStore();

    /**
     * 获取元数据存储策略对象
     * @return 元数据存储对象
     */
    public static MetaStore getMetaStore() {
        return metaStore;
    }
}