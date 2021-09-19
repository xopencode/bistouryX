package qunar.tc.bistoury.agent.common.kv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author 肖哥弹架构
 * @date 2022-09-12
 * @desc 数据源管理工具
 */
public class DataSourceHelper {
    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(DataSourceHelper.class);

    /**
     * 关闭数据源
     * @param resultSet 结果集
     * @param statement 声明
     * @param connection 连接
     */
    public static void closeResource(ResultSet resultSet, Statement statement, Connection connection) {
        // 关闭结果集
        // ctrl+alt+m 将java语句抽取成方法
        closeResource(resultSet);
        // 关闭语句执行者
        closeResource(statement);
        // 关闭连接
        closeResource(connection);
    }

    /**
     * 关闭结果集与声明数据源
     * @param resultSet 结果集
     * @param statement 声明
     */
    public static void closeResource(ResultSet resultSet, Statement statement) {
        // 关闭结果集
        // ctrl+alt+m 将java语句抽取成方法
        closeResource(resultSet);
        // 关闭语句执行者
        closeResource(statement);
    }

    /**
     * 关闭 声明与连接数据源
     * @param statement 声明
     * @param connection 连接
     */
    public static void closeResource(Statement statement, Connection connection) {
        // 关闭语句执行者
        closeResource(statement);
        // 关闭连接
        closeResource(connection);
    }

    /**
     * 关闭连接数据源
     * @param connection 连接
     */
    public static void closeResource(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOG.error("close connection error", e);
            }
        }
    }

    /**
     *  关闭声明数据源
     * @param statement 声明
     */
    public static void closeResource(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOG.error("close statement error", e);
            }
        }
    }

    /**
     * 关闭结果集数据源
     * @param resultSet 结果集
     */
    public static void closeResource(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOG.error("close ResultSet error", e);
            }
        }
    }
}
