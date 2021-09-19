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

import com.google.common.collect.ImmutableMap;

import java.util.Date;
import java.util.Map;

/**
 * @author 肖哥弹架构
 * @date 2022-09-13
 * @desc 默认Agent元数据存储策略
 */
public class DefaultMetaStore implements MetaStore {
    /**
     * Agent元数据存储容器
     */
    private volatile Map<String, String> attrs = ImmutableMap.of();

    DefaultMetaStore() { }

    /**
     * 更新元数据存储容器数据
     * @param attrs 元数据KV
     */
    @Override
    public void update(Map<String, String> attrs) {
        this.attrs = ImmutableMap.copyOf(attrs);
    }

    /**
     * 获取Agent所有属性信息
     * @return
     */
    @Override
    public Map<String, String> getAgentInfo() {
        return this.attrs;
    }

    /**
     * 获取字符串属性值
     * @param name
     * @return
     */
    @Override
    public String getStringProperty(String name) {
        return getStringProperty(name, null);
    }

    /**
     * 获取字符串属性值，无则返回默认值
     * @param name
     * @param def
     * @return
     */
    @Override
    public String getStringProperty(String name, String def) {
        String v = attrs.get(name);
        if (v == null) {
            return def;
        }
        return v;
    }

    /**
     * 获取BOOLEAN值属性值
     * @param name
     * @return
     */
    @Override
    public boolean getBooleanProperty(String name) {
        return getBooleanProperty(name, false);
    }

    /**
     * 获取Boolean属性值，无则返回默认值
     * @param name
     * @param def
     * @return
     */
    @Override
    public boolean getBooleanProperty(String name, boolean def) {
        String v = attrs.get(name);
        if (v == null || v.isEmpty()) {
            return def;
        }
        return Boolean.parseBoolean(v.trim());
    }

    /**
     * 返回日期属性值
     * @param name
     * @return
     */
    @Override
    public Date getDateProperty(String name) {
        String o = attrs.get(name);
        if (o == null) {
            return null;
        }
        Long v = Long.valueOf(o);
        return new Date(v);
    }

    /**
     * 返回整数属性值
     * @param name
     * @return
     */
    @Override
    public int getIntProperty(String name) {
        return getIntProperty(name, 0);
    }

    /**
     * 返回整数属性值，无则返回默认值
     * @param name
     * @param def
     * @return
     */
    @Override
    public int getIntProperty(String name, int def) {
        String o = attrs.get(name);
        return Numbers.toInt(o, def);
    }

    /**
     * 返回Long属性值，无则返回默认值
     * @param name
     * @return
     */
    @Override
    public long getLongProperty(String name) {
        return getLongProperty(name, 0);
    }

    /**
     * 返回Long属性值，无则返回默认值
     * @param name
     * @param def
     * @return
     */
    @Override
    public long getLongProperty(String name, long def) {
        String o = attrs.get(name);
        return Numbers.toLong(o, def);
    }

    /**
     * 返回Float属性值
     * @param name
     * @return
     */
    @Override
    public float getFloatProperty(String name) {
        return getFloatProperty(name, 0);
    }

    /**
     * 返回Float属性值，无则返回默认值
     * @param name
     * @param def
     * @return
     */
    @Override
    public float getFloatProperty(String name, float def) {
        String o = attrs.get(name);
        return Numbers.toFloat(o, def);
    }

    /**
     * 返回Double属性值
     * @param name
     * @return
     */
    @Override
    public double getDoubleProperty(String name) {
        return getDoubleProperty(name, 0);
    }

    /**
     * 返回Double属性值,无则返回默认值
     * @param name
     * @param def
     * @return
     */
    @Override
    public double getDoubleProperty(String name, double def) {
        String o = attrs.get(name);
        return Numbers.toDouble(o, def);
    }
}
