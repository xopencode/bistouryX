package qunar.tc.bistoury.agent.common;

import java.util.Properties;

/**
 * @author 肖哥弹架构
 * @date 2022-09-11
 * @desc Java版本工具
 */
public class JavaVersionUtils {
    /**
     * JVM 版本属性名
     */
    private static final String VERSION_PROP_NAME = "java.specification.version";
    /**
     * 版本属性键
     */
    private static final String JAVA_VERSION_STR = System.getProperty(VERSION_PROP_NAME);
    /**
     * 版本值
     */
    private static final float JAVA_VERSION = Float.parseFloat(JAVA_VERSION_STR);

    /**
     * JAVA版本属性键
     * @return
     */
    public static String javaVersionStr() {
        return JAVA_VERSION_STR;
    }

    /**
     * JAVA版本属性值
     * @param props
     * @return
     */
    public static String javaVersionStr(Properties props) {
        return (null != props) ? props.getProperty(VERSION_PROP_NAME) : null;
    }

    /**
     * JAVA版本
     * @return
     */
    public static float javaVersion() {
        return JAVA_VERSION;
    }

    /**
     * @return 判断是否为JAVA 1.6版本
     */
    public static boolean isJava6() {
        return JAVA_VERSION_STR.equals("1.6");
    }

    /**
     * @return  判断是否为JAVA 1.7版本
     */
    public static boolean isJava7() {
        return JAVA_VERSION_STR.equals("1.7");
    }

    /**
     * @return  判断是否为JAVA 1.8版本
     */
    public static boolean isJava8() {
        return JAVA_VERSION_STR.equals("1.8");
    }

    /**
     * @return  判断是否为JAVA 1.9版本
     */
    public static boolean isJava9() {
        return JAVA_VERSION_STR.equals("9");
    }

    /**
     * @return  判断是否为JAVA 1.9版本以下
     */
    public static boolean isLessThanJava9() {
        return JAVA_VERSION < 9.0f;
    }

    /**
     * @return   判断是否为JAVA 1.8版本以上
     */
    public static boolean isGreaterThanJava8() {
        return JAVA_VERSION > 1.8f;
    }

    /**
     * @return   判断是否为JAVA 1.8 版本或以上
     */
    public static boolean isGreaterThanOrEqualToJava8() {
        return JAVA_VERSION >= 1.8f;
    }

    public static void main(String[] args) {
        System.out.println(JAVA_VERSION_STR);
        System.out.println(JAVA_VERSION);
    }
}
