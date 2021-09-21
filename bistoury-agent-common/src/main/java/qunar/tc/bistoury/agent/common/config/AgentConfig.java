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

package qunar.tc.bistoury.agent.common.config;

import qunar.tc.bistoury.clientside.common.meta.MetaStore;

/**
 * @author 肖哥弹架构
 * @date 2022-09-12
 * @desc Agent配置
 */
public class AgentConfig {

    /**
     * Agent元数据存储
     */
    private final MetaStore metaStore;

    public AgentConfig(MetaStore metaStore) {
        this.metaStore = metaStore;
    }

    /**
     * 是否开启堆直方图
     * @return
     */
    public boolean isHeapHistoOn() {
        return metaStore.getBooleanProperty("heapJMapHistoOn", false);
    }

    /**
     * 获取内存堆直方图存储大小
     * @return
     */
    public int getHeapHistoStoreSize() {
        return metaStore.getIntProperty("heapHisto.store.size", 100);
    }

    /**
     * 获取Agent信息刷新间隔
     * @return
     */
    public int getAgentInfoRefreshInterval() {
        return metaStore.getIntProperty("agent.refresh.interval.min", 10);
    }

    /**
     * 是否开启CPU Stack功能
     * @return
     */
    public boolean isCpuJStackOn() {
        return metaStore.getBooleanProperty("cpuJStackOn", false);
    }

}
