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

/**
 * @author 肖哥弹架构
 * @date 2022-09-11
 * @desc 用于计算一段时间内的计量，通常用于计算接口调用频率
 */
public interface BistouryMeter {
    /**
     * 度量
     */
    void mark();

    /**
     * 度量 +n
     * @param n 值
     */
    void mark(long n);
}
