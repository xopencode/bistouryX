package qunar.tc.bistoury.serverside.common;

import java.io.File;

/**
 * @author 肖哥弹架构
 * @date 2022-09-11
 * @desc Bistoury 服务常量定义
 */
public class BistouryServerConstants {
    /**
     * 性能分析数据存放地址
     */
    public static final String PROFILER_ROOT_PATH = System.getProperty("java.io.tmpdir") + File.separator + "bistoury-profiler";
    /**
     *  性能分析数据存放临时地址
     */
    public static final String PROFILER_ROOT_TEMP_PATH = PROFILER_ROOT_PATH + File.separator + "tmp";
    /**
     * 性能分析数据存放Agent地址
     */
    public static final String PROFILER_ROOT_AGENT_PATH = PROFILER_ROOT_PATH + File.separator + "agent";
}
