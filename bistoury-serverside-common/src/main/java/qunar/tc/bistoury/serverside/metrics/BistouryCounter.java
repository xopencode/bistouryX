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
 * @desc 计数类统计，可以进行加或减，也可以进行归零操作，所 有的操作都是在旧值的基础上进行的
 */
public interface BistouryCounter {
    /**
     * +1
     */
    void inc();

    /**
     * 增加指定数
     * @param n +n
     */
    void inc(long n);

    /**
     * -1
     */
    void dec();

    /**
     * 减少指定数
     * @param n -n
     */
    void dec(long n);
}
