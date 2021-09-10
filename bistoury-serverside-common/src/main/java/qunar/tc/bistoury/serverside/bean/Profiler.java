package qunar.tc.bistoury.serverside.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

/**
 * @author 肖哥弹架构
 * @date 2019/7/2 16:03
 * @describe API请求结果包装类
 */
public class Profiler {
    /**
     * 编号
     */
    private long id;
    /**
     * 性能优化编号
     */
    private String profilerId;
    /**
     * 操作者
     */
    private String operator;
    /**
     * 应用编号
     */
    private String appCode;
    /**
     * agent编号
     */
    private String agentId;
    /**
     * 应用进程编号
     */
    private int pid;

    /**
     * 开始时间
     */
    private Timestamp startTime;
    /**
     * 更新时间
     */
    private Timestamp updateTime;
    /**
     * 运行状态
     */
    private State state;
    /**
     * 持续时间
     */
    private int duration;
    /**
     * 时间间隔
     */
    private int interval;
    /**
     * 采样模型
     */
    private Mode mode;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProfilerId() {
        return profilerId;
    }

    public void setProfilerId(String profilerId) {
        this.profilerId = profilerId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /**
     * 状态枚举
     */
    public enum State {
        /**
         * 开始状态
         */
        start(0),
        /**
         * 停止状态
         */
        stop(1),
        /**
         * 就绪状态
         */
        ready(2),
        /**
         * 分析状态
         */
        analyzed(3),
        error(4);
        /**
         * 枚举值
         */
        public final int code;

        State(int code) {
            this.code = code;
        }

        public static State fromCode(int code) {
            for (State state : values()) {
                if (state.code == code) {
                    return state;
                }
            }
            throw new IllegalArgumentException("no code found in State.");
        }
    }

    /**
     * 模型
     */
    public enum Mode {
        /**
         * 同步采样
         */
        sampler(1),
        /**
         * 异步采样
         */
        async_sampler(0);
        /**
         * 枚举值
         */
        public final int code;

        Mode(int code) {
            this.code = code;
        }

        public static Mode fromCode(int code) {
            for (Mode mode : values()) {
                if (mode.code == code) {
                    return mode;
                }
            }
            throw new IllegalArgumentException("no code found in Mode.");
        }
    }
}
