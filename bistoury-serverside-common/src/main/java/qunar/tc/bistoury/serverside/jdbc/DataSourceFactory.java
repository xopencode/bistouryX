package qunar.tc.bistoury.serverside.jdbc;

import javax.sql.DataSource;

import qunar.tc.bistoury.serverside.configuration.DynamicConfig;

/**
 * @author 肖哥弹架构
 * @date 2022-09-11
 * @desc 数据库工厂
 */
public interface DataSourceFactory {
    /**
     * 创建关系型数据源
     * @param dynamicConfig 动态配置对象
     * @return 数据源
     */
	DataSource createDataSource(DynamicConfig dynamicConfig);

}
