package com.lyl.layuiadmin.config;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * 读取小贷系统数据源<br>
 *
 */
@Configuration
//此处是你dao文件所在的包名
@EnableJpaRepositories(basePackages = "com.lyl.layuiadmin.Dao", entityManagerFactoryRef = "entityManagerFactory")
@EnableTransactionManagement
public class DataSourceCreditConfig {

	@Autowired
	Environment environment;

	@Bean(name = "creditDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.credit")
	public DataSource creditDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "entityManagerFactory")
	public EntityManagerFactory entityManagerFactory(@Qualifier("creditDataSource") DataSource dataSource) {

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		// 设置是否在初始化EntityManagerFactory后生成DDL，创建/更新所有相关表。
		//		vendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setJpaProperties(hibernateProperties());
		factory.setPackagesToScan("com.lyl");
		factory.setDataSource(dataSource);
		factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Bean
	public Properties hibernateProperties() {

		Properties jpaProperties = new Properties();
		String showSql = environment.getRequiredProperty("spring.jpa.show-sql");
		String ddlAuto = environment.getRequiredProperty("spring.jpa.hibernate.ddl-auto");
		String physical = environment.getRequiredProperty("spring.jpa.hibernate.naming.physical-strategy");
		String implicit = environment.getRequiredProperty("spring.jpa.hibernate.naming.implicit-strategy");
		String dialect = environment.getRequiredProperty("spring.jpa.properties.hibernate.dialect");
		// 拦截器
		jpaProperties.put(AvailableSettings.SESSION_SCOPED_INTERCEPTOR,"com.lyl.layuiadmin.common.BizEmptyInterceptor");
		// 方言
		jpaProperties.put(AvailableSettings.DIALECT, dialect);
		// 自动建表
		jpaProperties.put(AvailableSettings.HBM2DDL_AUTO, ddlAuto);
		// 命名规则
		jpaProperties.put(AvailableSettings.IMPLICIT_NAMING_STRATEGY, implicit);
		jpaProperties.put(AvailableSettings.PHYSICAL_NAMING_STRATEGY, physical);
		// 打印sql
		jpaProperties.put(AvailableSettings.SHOW_SQL, showSql);
		// 格式化sql
		jpaProperties.put(AvailableSettings.FORMAT_SQL, environment.getRequiredProperty("spring.jpa.format-sql"));
		return jpaProperties;
	}

	/** 事务管理 */
	@Bean
	public PlatformTransactionManager transactionManager(
			@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory);
		return txManager;
	}

}
