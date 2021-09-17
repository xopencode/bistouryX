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

import com.google.common.base.Supplier;

import java.util.concurrent.TimeUnit;

/**
 * @author 肖哥弹架构
 * @date 2022-09-11
 * @desc Mock 度量信息注册管理
 */
class MockRegistry implements BistouryMetricRegistry {

    /**
     * 实例化 MockCounter
     */
    private static final BistouryCounter COUNTER = new MockCounter();
    /**
     * 实例化  MockMeter
     */
    private static final BistouryMeter METER = new MockMeter();
    /**
     * 实例化 MockTimer
     */
    private static final BistouryTimer TIMER = new MockTimer();

    /**
     * 不支持
     * @param name 项名
     * @param tags 标记
     * @param values 值
     * @param supplier 回调函数
     */
    @Override
    public void newGauge(String name, String[] tags, String[] values, Supplier<Double> supplier) {

    }

    @Override
    public BistouryCounter newCounter(String name, String[] tags, String[] values) {
        return COUNTER;
    }

    @Override
    public BistouryMeter newMeter(String name, String[] tags, String[] values) {
        return METER;
    }

    @Override
    public BistouryTimer newTimer(String name, String[] tags, String[] values) {
        return TIMER;
    }

    /**
     * 不支持
     * @param name 项名
     * @param tags 标记
     * @param values 值
     */
    @Override
    public void remove(String name, String[] tags, String[] values) {

    }

    /**
     * MockCounter类
     */
    private static class MockCounter implements BistouryCounter {

        @Override
        public void inc() {

        }

        @Override
        public void inc(long n) {

        }

        @Override
        public void dec() {

        }

        @Override
        public void dec(long n) {

        }
    }

    /**
     * MockMeter类
     */
    private static class MockMeter implements BistouryMeter {

        @Override
        public void mark() {

        }

        @Override
        public void mark(long n) {

        }
    }

    /**
     * MockTimer类
     */
    private static class MockTimer implements BistouryTimer {

        @Override
        public void update(long duration, TimeUnit unit) {

        }
    }
}
