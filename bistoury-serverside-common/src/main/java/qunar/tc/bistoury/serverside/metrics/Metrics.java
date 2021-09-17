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

package qunar.tc.bistoury.serverside.metrics;

import com.google.common.base.Supplier;

import java.util.ServiceLoader;

/**
 * @author 肖哥弹架构
 * @date 2022-09-11
 * @desc 度量管理类
 */
public class Metrics {
    /**
     * 空值
     */
    private static final String[] EMPTY = new String[0];
    /**
     * 度量注册管理实例
     */
    private static final BistouryMetricRegistry INSTANCE;

    /**
     * 静态代码块
     */
    static {
         //通过插件SPI机制获取BistouryMetricRegistry实现提供者
        ServiceLoader<BistouryMetricRegistry> registries = ServiceLoader.load(BistouryMetricRegistry.class);
        //度量注册实例
        BistouryMetricRegistry instance = null;
        for (BistouryMetricRegistry registry : registries) {
            instance = registry;
        }
        //如果没有插件提供者则返回Mock对象
        if (instance == null) {
            instance = new MockRegistry();
        }
        INSTANCE = instance;
    }

    /**
     * 业务项瞬时值的测量
     * @param name 业务名
     * @param tags 标记名
     * @param values 标记值
     * @param supplier 回调函数
     */
    public static void gauge(String name, String[] tags, String[] values, Supplier<Double> supplier) {
        INSTANCE.newGauge(name, tags, values, supplier);
    }

    /**
     * 业务项瞬时空值测量
     * @param name 业务项
     * @param supplier 回调函数
     */
    public static void gauge(String name, Supplier<Double> supplier) {
        INSTANCE.newGauge(name, EMPTY, EMPTY, supplier);
    }

    /**
     * 业务项统计值
     * @param tags 标记
     * @param values 标记值
     * @return BistouryCounter
     */
    public static BistouryCounter counter(String name, String[] tags, String[] values) {
        return INSTANCE.newCounter(name, tags, values);
    }
    /**
     * 业务项统计空值
     * @param name 业务项
     */
    public static BistouryCounter counter(String name) {
        return INSTANCE.newCounter(name, EMPTY, EMPTY);
    }

    /**
     * 业务项时间段内值统计
     * @param name 业务项
     * @param tags 标记
     * @param values 标记值
     * @return BistouryMeter
     */
    public static BistouryMeter meter(String name, String[] tags, String[] values) {
        return INSTANCE.newMeter(name, tags, values);
    }
    /**
     * 业务项时间段内的空值统计
     * @param name 业务项
     */
    public static BistouryMeter meter(String name) {
        return INSTANCE.newMeter(name, EMPTY, EMPTY);
    }

    /**
     * 业务项时间段内值统计
     * @param name 业务项
     * @param tags 标记
     * @param values 标记值
     * @return BistouryTimer
     */
    public static BistouryTimer timer(String name, String[] tags, String[] values) {
        return INSTANCE.newTimer(name, tags, values);
    }
    /**
     * 记录业务项时间单位内空值
     * @param name 业务项
     */
    public static BistouryTimer timer(String name) {
        return INSTANCE.newTimer(name, EMPTY, EMPTY);
    }

    /**
     * 删除统计值
     * @param name 业务项
     * @param tags 标记
     * @param values 标记值
     */
    public static void remove(String name, String[] tags, String[] values) {
        INSTANCE.remove(name, tags, values);
    }
}
