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

/**
 * @author 肖哥弹架构
 * @date 2022-09-11
 * @desc 度量信息注册
 */
public interface BistouryMetricRegistry {
    /**
     * 创建指定业务项瞬时值的测量
     * @param name 项名
     * @param tags 标记
     * @param values 值
     * @param supplier 回调函数
     */
    void newGauge(final String name, final String[] tags, final String[] values, final Supplier<Double> supplier);

    /**
     *  创建指定业务项总数统计
     * @param name 项名
     * @param tags 标记
     * @param values 值
     * @return BistouryCounter
     */
    BistouryCounter newCounter(final String name, final String[] tags, final String[] values);

    /**
     * 创建指定业务项某时间维度计量
     * @param name 项名
     * @param tags 标记
     * @param values 值
     * @return BistouryMeter
     */
    BistouryMeter newMeter(final String name, final String[] tags, final String[] values);

    /**
     * 创建指定业务项时间指标统计
     * @param name 项名
     * @param tags 标记
     * @param values 值
     * @return BistouryTimer
     */
    BistouryTimer newTimer(final String name, final String[] tags, final String[] values);

    /**
     * 删除某业务项统计值
     * @param name 项名
     * @param tags 标记
     * @param values 值
     */
    void remove(final String name, final String[] tags, final String[] values);
}
