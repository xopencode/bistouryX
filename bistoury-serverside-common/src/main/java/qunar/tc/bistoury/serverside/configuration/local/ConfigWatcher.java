
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

package qunar.tc.bistoury.serverside.configuration.local;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qunar.tc.bistoury.common.NamedThreadFactory;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author 肖哥弹架构
 * @date 2022-09-10
 * @desc 配置文件修改监听者
 */
class ConfigWatcher {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigWatcher.class);
    /**
     * 看门实体对象容器
     */
    private final CopyOnWriteArrayList<Watch> watches;
    /**
     * 定时线程池服务
     */
    private final ScheduledExecutorService watcherExecutor;

    ConfigWatcher() {
        //watch容器
        this.watches = new CopyOnWriteArrayList<>();
        //线程池
        this.watcherExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("local-config-watcher"));
        //开启文件更新扫描
        start();
    }

    private void start() {
        watcherExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                //检测所有看门的配置文件
                checkAllWatches();
            }
        }, 10, 10, TimeUnit.SECONDS);//10秒扫描
    }

    /**
     * 检测所有的看门配置文件
     */
    private void checkAllWatches() {
        for (Watch watch : watches) {
            try {
                checkWatch(watch);
            } catch (Exception e) {
                LOG.error("check config failed. config: {}", watch.getConfig(), e);
            }
        }
    }

    /**
     * 检测某个看门配置对象
     * @param watch 看门配置对象
     */
    private void checkWatch(final Watch watch) {
        //本地动态配置对象
        final LocalDynamicConfig config = watch.getConfig();
        //最后修改时间
        final long lastModified = config.getLastModified();
        //当前配置文件最后修改时间不等于看门最后修改时间则进行重新加载
        if (lastModified == watch.getLastModified()) {
            return;
        }
        //设置最新的看门时间
        watch.setLastModified(lastModified);
        //执行配置修改加载
        config.onConfigModified();
    }

    /**
     * 添加看门本地动态配置对象
     * @param config 本地动态配置对象
     */
    void addWatch(final LocalDynamicConfig config) {
        final Watch watch = new Watch(config);
        watch.setLastModified(config.getLastModified());
        watches.add(watch);
    }

    /**
     * 看门实体类
     */
    private static final class Watch {
        /**
         * 本地动态配置
         */
        private final LocalDynamicConfig config;
        /**
         * 最后修改对象
         */
        private volatile long lastModified;

        private Watch(final LocalDynamicConfig config) {
            this.config = config;
        }

        public LocalDynamicConfig getConfig() {
            return config;
        }

        long getLastModified() {
            return lastModified;
        }

        void setLastModified(final long lastModified) {
            this.lastModified = lastModified;
        }
    }
}
