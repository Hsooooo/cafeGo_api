package co.kr.cafego.core.config;

import java.util.Hashtable;
import java.util.Properties;

import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import com.ezens.starbucks.biz.util.TripleDes;

@Configuration
@PropertySource({
	"classpath:db.properties"
})
@EnableTransactionManagement
public class DatabaseConfig{
	
	private static final Logger LOGGER = LoggerFactory.getLogger("INFO");
	
	@Inject
	private Environment env;
	
	@Bean(name="dataSource")
	public DataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty("db.driver.className", ""));
		dataSource.setUrl(env.getProperty("db.url", ""));
		dataSource.setUsername(env.getProperty("db.user.name", ""));
		dataSource.setPassword(env.getProperty("db.user.pwd", ""));
		
		return dataSource;
	}
	
	
	@Bean(name="sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
		PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setConfigLocation(resourceResolver.getResource(getConfigLocation()));
        factoryBean.setMapperLocations(resourceResolver.getResources(getMapperLocationPattern()));
        factoryBean.setConfigurationProperties(getConfigurationProperties());
        return factoryBean.getObject();
    }
	
	@Bean(name="sqlSession")
    public SqlSessionTemplate sqlSession(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
    	return new SqlSessionTemplate(sqlSessionFactory);
    }

	
	@Bean(name="transactionManager")
	public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
	
	@Bean(name="transactionTemplate")
	public TransactionTemplate transactionTemplate(@Qualifier("transactionManager") PlatformTransactionManager transactionManager){
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		return transactionTemplate;
	}

	
	@Bean
	public MapperScannerConfigurer mapperScannerConfigurer(){
		MapperScannerConfigurer mc = new MapperScannerConfigurer();
		mc.setNameGenerator(new CafegoBeanNameGenerator());
		mc.setBasePackage("co.kr.cafego.api.**");
		mc.setSqlSessionFactoryBeanName("sqlSessionFactory");
		return mc;
	}
	
	protected String getMapperLocationPattern() {
		return "classpath:co/kr/cafego/api/**/*Mapper.xml";
	}
	
	protected String getConfigLocation() {
		return "classpath:mybatis-config.xml";
	}

	protected Properties getConfigurationProperties() {
		return null;
	}
	//###################################### Jndi 방식 대비 샘플 ######################################
	
	@SuppressWarnings("rawtypes")
	public static Hashtable jndiContext(){
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put(Context.INITIAL_CONTEXT_FACTORY,	"jeus.jndi.JNSContextFactory");
		ht.put(Context.URL_PKG_PREFIXES, 		"jeus.jndi.jns.url");

//		if("local".equals(mode)) {
//			ht.put(Context.PROVIDER_URL, 			"127.0.0.1:9738");
//			ht.put(Context.SECURITY_PRINCIPAL, 		"administrator");
//			ht.put(Context.SECURITY_CREDENTIALS, 	"rkrk6469");
//		} else {
			ht.put(Context.PROVIDER_URL, 		 System.getProperty("das.ip"));
			ht.put(Context.SECURITY_PRINCIPAL, 	 TripleDes.decrypt("GpY67VzJoQE="));
			ht.put(Context.SECURITY_CREDENTIALS, TripleDes.decrypt("JGNtwEZPmi572P7ekqk2VA=="));
//		}
		
		return ht;
	}
	
	
	/**
	 * ?��?��?��?��?��_DB JNDI
	 */
	@Bean(name="dataSourceXO")
	public DataSource dataSourceXO(){
		DataSource dataSource = null;
		
		try {
			Context ctx = new InitialContext(DatabaseConfig.jndiContext());
			dataSource = (DataSource)ctx.lookup("xojndi");
		} catch (NamingException ne) {
			LOGGER.error(ne.getMessage().replaceAll("\n|\r", ""), ne);
		}
		return dataSource;
	}
	@Bean(name="sqlSessionFactoryXO")
    public SqlSessionFactory sqlSessionFactoryXO(@Qualifier("dataSourceXO") DataSource dataSourceXO) throws Exception {
		PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSourceXO);
		factoryBean.setConfigLocation(resourceResolver.getResource(getConfigLocation()));
        factoryBean.setMapperLocations(resourceResolver.getResources(getMapperLocationPatternXO()));
        factoryBean.setConfigurationProperties(getConfigurationProperties());
        return factoryBean.getObject();
    }
	
	@Bean(name="sqlSessionXO")
    public SqlSessionTemplate sqlSessionXO(@Qualifier("sqlSessionFactoryXO") SqlSessionFactory sqlSessionFactoryXO) {
    	return new SqlSessionTemplate(sqlSessionFactoryXO);
    }
	
	/**
	 * XO TransactionManager
	 * @param dataSourceMSR
	 * @return
	 */
	@Bean(name="transactionManagerXO")
	public PlatformTransactionManager transactionManagerXO(@Qualifier("dataSourceXO") DataSource dataSourceXO) {
		return new DataSourceTransactionManager(dataSourceXO);
	}
	
	@Bean(name="transactionTemplateXO")
	public TransactionTemplate transactionTemplateXO(@Qualifier("transactionManagerXO") PlatformTransactionManager transactionManagerXO){
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManagerXO);
		return transactionTemplate;
	}
	
	/**
	 * ?��?��?��?��?�� Mapper Scan
	 * package : co.kr.istarbucks.xo.**
	 * @return
	 */
	@Bean
	public MapperScannerConfigurer mapperScannerConfigurerXO(){
		MapperScannerConfigurer mc = new MapperScannerConfigurer();
		mc.setNameGenerator(new CafegoBeanNameGenerator());
		mc.setBasePackage("co.kr.istarbucks.xo.**");
		mc.setSqlSessionFactoryBeanName("sqlSessionFactoryXO");
		return mc;
	}
	protected String getMapperLocationPatternXO() {
		return "classpath:co/kr/istarbucks/xo/**/*Mapper.xml";
	}
	
	//###################################### Jndi 방식 대비 샘플 ######################################
	
}
