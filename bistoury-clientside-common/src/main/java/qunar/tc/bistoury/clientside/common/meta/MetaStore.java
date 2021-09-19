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

import java.util.Date;
import java.util.Map;

/**
 * @author 肖哥弹架构
 * @date 2022-09-13
 * @desc 元数据存储策略
 */
public interface MetaStore {
    /**
     * 更新元数据
     * @param attrs 元数据KV
     */
    void update(Map<String, String> attrs);

    /**
     * 获取代理信息
     * @return 代理信息
     */
    Map<String, String> getAgentInfo();

    /**
     * 获取字符串属性值
     * @param name
     * @return
     */
    String getStringProperty(String name);

    /**
     *  获取字符串属性值，无则返回默认值
     * @param name
     * @param def
     * @return
     */
    String getStringProperty(String name,String def);

    /**
     * 获取BOOLEAN值属性值
     * @param name
     * @return
     */
    boolean getBooleanProperty(String name);

    /**
     * 获取Boolean属性值，无则返回默认值
     * @param name
     * @param def
     * @return
     */
    boolean getBooleanProperty(String name, boolean def);

    /**
     * 返回日期属性值
     * @param name
     * @return
     */
    Date getDateProperty(String name);

    /**
     * 返回整数属性值
     * @param name
     * @return
     */
    int getIntProperty(String name);

    /**
     * 返回整数属性值，无则返回默认值
     * @param name
     * @param def
     * @return
     */
    int getIntProperty(String name, int def);

    /**
     * 返回Long属性值，无则返回默认值
     * @param name
     * @return
     */
    long getLongProperty(String name);

    /**
     * 返回Long属性值，无则返回默认值
     * @param name
     * @param def
     * @return
     */
    long getLongProperty(String name, long def);

    /**
     * 返回Float属性值
     * @param name
     * @return
     */
    float getFloatProperty(String name);

    /**
     * 返回Float属性值，无则返回默认值
     * @param name
     * @param def
     * @return
     */
    float getFloatProperty(String name, float def);

    /**
     * 返回Double属性值
     * @param name
     * @return
     */
    double getDoubleProperty(String name);

    /**
     * 返回Double属性值,无则返回默认值
     * @param name
     * @param def
     * @return
     */
    double getDoubleProperty(String name, double def);
}
