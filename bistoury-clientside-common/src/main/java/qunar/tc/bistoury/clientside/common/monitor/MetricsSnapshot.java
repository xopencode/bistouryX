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

package qunar.tc.bistoury.clientside.common.monitor;

import java.util.List;

/**
 * @author 肖哥弹架构
 * @date 2022-09-14
 * @desc 度量数据快照
 */
public class MetricsSnapshot {
    /**
     * 度量快照名
     */
    private String name;
    /**
     * 快照时间
     */
    private Long timestamp;
    /**
     * 度量数据
     */
    private List<MetricsData> metricsData;

    public MetricsSnapshot() { }

    public MetricsSnapshot(String name, Long timestamp, List<MetricsData> metricsData) {
        this.name = name;
        this.timestamp = timestamp;
        this.metricsData = metricsData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public List<MetricsData> getMetricsData() {
        return metricsData;
    }

    public void setMetricsData(List<MetricsData> metricsData) {
        this.metricsData = metricsData;
    }
}
