package qunar.tc.bistoury.serverside.jdbc;

import java.util.ServiceLoader;

import javax.sql.DataSource;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import qunar.tc.bistoury.serverside.configuration.DynamicConfig;
import qunar.tc.bistoury.serverside.configuration.DynamicConfigLoader;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * @author 肖哥弹架构
 * @date 2022-09-11
 * @desc JDBC模板持有者模式
 */
public class JdbcTemplateHolder {
	/**
	 * 数据库配置文件名
	 */
	private static final String DEFAULT_DATASOURCE_CONF = "jdbc.properties";

	private static final Supplier<DataSource> DS_SUPPLIER = Suppliers.memoize(JdbcTemplateHolder::createDataSource);

	private static final Supplier<JdbcTemplate> JDBC_TEMPLATE_SUPPLIER = Suppliers.memoize(JdbcTemplateHolder::createJdbcTemplate);

	private static final Supplier<NamedParameterJdbcTemplate> NAMED_PARAMETER_JDBC_TEMPLATE_SUPPLIER =
			Suppliers.memoize(JdbcTemplateHolder::createNamedParameterJdbcTemplate);

    /**
     * @return 创建JdbcTemplate
     */
	private static JdbcTemplate createJdbcTemplate() {
		return new JdbcTemplate(DS_SUPPLIER.get());
	}

    /**
     * @return 创建NamedParameterJdbcTemplate对象
     */
	private static NamedParameterJdbcTemplate createNamedParameterJdbcTemplate() {
		return new NamedParameterJdbcTemplate(DS_SUPPLIER.get());
	}

    /**
     * @return 创建数据源对象
     */
	private static DataSource createDataSource() {
	    //加载jdbc.properties配置
		final DynamicConfig config = DynamicConfigLoader.load(DEFAULT_DATASOURCE_CONF);
		//通过插件SPI机制加载DataSourceFactory接口实现类
		ServiceLoader<DataSourceFactory> factories = ServiceLoader.load(DataSourceFactory.class);
		for (DataSourceFactory factory : factories) {
			return factory.createDataSource(config);//通过数据源工厂创建实现类
		}
        //如果没有数据源 SPI提供者,那么就提供默认数据源工厂创建数据源对象
		return new DefaultDataSourceFactory().createDataSource(config);
	}

    /**
     * @return 获取或创建JdbcTemplate
     */
	public static JdbcTemplate getOrCreateJdbcTemplate() {
		return JDBC_TEMPLATE_SUPPLIER.get();
	}

    /**
     * @return  创建NamedParameterJdbcTemplate对象
     */
	public static NamedParameterJdbcTemplate getOrCreateNamedParameterJdbcTemplate() {
		return NAMED_PARAMETER_JDBC_TEMPLATE_SUPPLIER.get();
	}

    /**
     * @return 获取数据源
     */
	public static DataSource getOrCreateDataSource() {
		return DS_SUPPLIER.get();
	}

}
