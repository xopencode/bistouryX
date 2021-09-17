package qunar.tc.bistoury.serverside.jdbc;

import javax.sql.DataSource;

import qunar.tc.bistoury.serverside.configuration.DynamicConfig;

/**
 * @author 肖哥弹架构
 * @date 2022-09-11
 * @desc 默认数据源工厂
 */
public class DefaultDataSourceFactory implements DataSourceFactory {
	/**
	 * 创建数据源
	 * @param dynamicConfig 动态配置对象
	 * @return 数据源
	 */
	@Override
	public DataSource createDataSource(DynamicConfig dynamicConfig) {
	    //tomcat 数据源定义
		org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
		//数据库驱动
		dataSource.setDriverClassName(dynamicConfig.getString("jdbc.driverClassName", "com.mysql.jdbc.Driver"));
		//数据库地址
		dataSource.setUrl(dynamicConfig.getString("jdbc.url"));
		//用户名
		dataSource.setUsername(dynamicConfig.getString("jdbc.username"));
		//密码
		dataSource.setPassword(dynamicConfig.getString("jdbc.password"));
		//最大活跃
		dataSource.setMaxActive(30);
		//最小闲置
		dataSource.setMinIdle(20);
		//最大等待
		dataSource.setMaxWait(3000);
		//服务存活检测
		dataSource.setValidationQuery("select 1");
		//是否启动测试
		dataSource.setTestOnBorrow(true);
		//是否要测试返回值
		dataSource.setTestOnReturn(true);
		return dataSource;
	}
}
