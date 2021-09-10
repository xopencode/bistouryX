package qunar.tc.bistoury.serverside.bean;

/**
 * @author 肖哥弹架构
 * @date 2019/7/2 16:03
 * @describe 性能设置
 */
public class ProfilerSettings {
    /**
     * 应用编号
     */
    private String appCode;
    /**
     * 持续时间
     */
    private int duration;
    /**
     * 时间间隔
     */
    private int interval;
    /**
     * 采用模型
     */
    private int mode;
    /**
     * 执行命令
     */
    private String command;

    public ProfilerSettings(String appCode) {
        this.appCode = appCode;
    }

    public ProfilerSettings(String appCode, int duration, int interval, int mode, String command) {
        this.appCode = appCode;
        this.duration = duration;
        this.interval = interval;
        this.mode = mode;
        this.command = command;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
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

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
