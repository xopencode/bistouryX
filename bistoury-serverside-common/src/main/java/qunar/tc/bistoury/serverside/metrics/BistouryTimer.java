/*
 * Copyright 2018 Qunar, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package qunar.tc.bistoury.serverside.metrics;

import java.util.concurrent.TimeUnit;

/**
 * @author 肖哥弹架构
 * @date 2022-09-11
 * @desc 是Histogram跟Meter的一个组合，比如要统计当前请求 的速率和处理时间
 */
public interface BistouryTimer {
    /**
     * 更新使用时间单位
     * @param duration 持续时间
     * @param unit 时间单位
     */
    void update(long duration, TimeUnit unit);
}
