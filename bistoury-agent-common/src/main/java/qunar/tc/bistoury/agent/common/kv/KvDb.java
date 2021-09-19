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

package qunar.tc.bistoury.agent.common.kv;

import java.util.Map;

/**
 * @author 肖哥弹架构
 * @date 2022-09-12
 * @desc KV数据库策略
 */
public interface KvDb {
    /**
     * 获取存储值
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 存储值
     * @param key 键
     * @param value 值
     */
    void put(String key,String value);

    /**
     * 批量添加值
     * @param data 数据
     */
    void putBatch(Map<String, String> data);
}
