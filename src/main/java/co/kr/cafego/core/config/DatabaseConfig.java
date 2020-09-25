package co.kr.cafego.core.config;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import com.ezens.starbucks.biz.util.TripleDes;

/**
 * DataBase  Í¥??†®?Ñ§?†ï
 * <p>
 * MSR - MSR DB
 * HP 	- ?ôà?éò?ù¥Ïß? DB
 * SALES - ?òÅ?óÖ DB (?Üµ?òÅ)
 * XO - ?Ç¨?ù¥?†å?ò§?çî DB
 * MSR_SALES - MSR+?òÅ?óÖ
 * </p>
 * @author Cha
 *
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfig{
	
	private static final Logger LOGGER = LoggerFactory.getLogger("INFO");
	
	// ?ö¥?òÅ_DB:real, Í∞úÎ∞ú_DB:dev, Î°úÏª¨_DB:local
//	private static String mode 	 =  SystemEnviroment.getActiveProfile();
	
	/*
	 * PROVIDER_URL : Í∞? ?ÑúÎ≤ÑÏùò JEUS_ADMIN_ADDRESS:JEUS_ADMIN_BASE_LISTEN_PORT Î°? ?Ñ§?†ï
	 * SECURITY_PRINCIPAL : JEUS_ADMIN ID
	 * SECURITY_CREDENTIALS : JEUS_ADMIN PASSWORD
	 */
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


	//###################################### DataSource ######################################
	/**
	 * MSR_DB JNDI
	 */
//	@Bean(name="dataSourceMSR")
//	public DataSource dataSourceMSR(){
//		DataSource dataSource = null;
//		
//		try {
//			Context ctx = new InitialContext(DatabaseConfig.jndiContext());
//			dataSource = (DataSource)ctx.lookup("msrjndi");
//		} catch (NamingException ne) {
//			logger.error(ne.getMessage(), ne);
//		}
//		return dataSource;
//	}	
	
	/**
	 * ?ôà?éò?ù¥Ïß?_DB JNDI
	 */
	@Bean(name="dataSourceHP")
	public DataSource dataSourceHP(){
		DataSource dataSource = null;
		
		try {
			Context ctx = new InitialContext(DatabaseConfig.jndiContext());
			dataSource = (DataSource)ctx.lookup("hpjndi");
		} catch (NamingException ne) {
			LOGGER.error(ne.getMessage().replaceAll("\n|\r", ""), ne);
		}
		return dataSource;
	}
	
//	/**
//	 * ?òÅ?óÖ?†ïÎ≥?_DB JNDI
//	 */
//	@Bean(name="dataSourceSALES")
//	public DataSource dataSourceSALES(){
//		DataSource dataSource = null;
//		
//		try {
//			Context ctx = new InitialContext(DatabaseConfig.jndiContext());
//			dataSource = (DataSource)ctx.lookup("sckjndi");
//		} catch (NamingException ne) {
//			logger.error(ne.getMessage(), ne);
//		}
//		return dataSource;
//	}
	
	/**
	 * ?Ç¨?ù¥?†å?ò§?çî_DB JNDI
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
	
//	/**
//	 * ?òÅ?óÖ?†ïÎ≥?_DB JNDI
//	 * ?ôòÎ∂àÏã†Ï≤? ?†ïÎ≥¥Ïö©
//	 */
//	@Bean(name="dataSourceSCKSC")
//	public DataSource dataSourceSCKSC(){
//		DataSource dataSource = null;
//		
//		try {
//			Context ctx = new InitialContext(DatabaseConfig.jndiContext());
//			dataSource = (DataSource)ctx.lookup("sckcdjndi");
//		} catch (NamingException ne) {
//			logger.error(ne.getMessage(), ne);
//		}
//		return dataSource;
//	}
//	
	/**
	 * OAuth2_DB JNDI
	 */
	@Bean(name="dataSourceOAUTH")
	public DataSource dataSourceOAUTH(){
		DataSource dataSource = null;
		
		try {
			Context ctx = new InitialContext(DatabaseConfig.jndiContext());
			dataSource = (DataSource)ctx.lookup("hp_auth_jndi");
		} catch (NamingException ne) {
			LOGGER.error(ne.getMessage().replaceAll("\n|\r", ""), ne);
		}
		return dataSource;
	}
	//###################################### DataSource ######################################
	
	
	
	//###################################### sqlSessionFactory ######################################
//	@Bean(name="sqlSessionFactoryMSR")
//    public SqlSessionFactory sqlSessionFactoryMSR(@Qualifier("dataSourceMSR") DataSource dataSourceMSR) throws Exception {
//		PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
//		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//		factoryBean.setDataSource(dataSourceMSR);
//		factoryBean.setConfigLocation(resourceResolver.getResource(getConfigLocation()));
//        factoryBean.setMapperLocations(resourceResolver.getResources(getMapperLocationPatternMSR()));
//        factoryBean.setConfigurationProperties(getConfigurationProperties());
//        return factoryBean.getObject();
//    }
	
//	@Bean(name="sqlSessionFactoryHP")
//    public SqlSessionFactory sqlSessionFactoryHP(@Qualifier("dataSourceHP") DataSource dataSourceHP) throws Exception {
//		PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
//		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//		factoryBean.setDataSource(dataSourceHP);
//		factoryBean.setConfigLocation(resourceResolver.getResource(getConfigLocation()));
//		factoryBean.setMapperLocations(resourceResolver.getResources(getMapperLocationPatternHP()));
//		factoryBean.setConfigurationProperties(getConfigurationProperties());
//		return factoryBean.getObject();
//	}
//	
//	@Bean(name="sqlSessionFactorySALES")
//    public SqlSessionFactory sqlSessionFactorySALES(@Qualifier("dataSourceSALES") DataSource dataSourceSALES) throws Exception {
//		PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
//		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//		factoryBean.setDataSource(dataSourceSALES);
//		factoryBean.setConfigLocation(resourceResolver.getResource(getConfigLocation()));
//        factoryBean.setMapperLocations(resourceResolver.getResources(getMapperLocationPatternSALES()));
//        factoryBean.setConfigurationProperties(getConfigurationProperties());
//        return factoryBean.getObject();
//    }
	
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
	
//	@Bean(name="sqlSessionFactorySCKSC")
//    public SqlSessionFactory sqlSessionFactorySCKSC(@Qualifier("dataSourceSCKSC") DataSource dataSourceSCKSC) throws Exception {
//		PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
//		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//		factoryBean.setDataSource(dataSourceSCKSC);
//		factoryBean.setConfigLocation(resourceResolver.getResource(getConfigLocation()));
//        factoryBean.setMapperLocations(resourceResolver.getResources(getMapperLocationPatternSCKSC()));
//        factoryBean.setConfigurationProperties(getConfigurationProperties());
//        return factoryBean.getObject();
//    }
//	
	@Bean(name="sqlSessionFactoryOAUTH")
    public SqlSessionFactory sqlSessionFactoryOAUTH(@Qualifier("dataSourceOAUTH") DataSource dataSourceOAUTH) throws Exception {
		PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSourceOAUTH);
		factoryBean.setConfigLocation(resourceResolver.getResource(getConfigLocation()));
		factoryBean.setMapperLocations(resourceResolver.getResources(getMapperLocationPatternOAUTH()));
		factoryBean.setConfigurationProperties(getConfigurationProperties());
		return factoryBean.getObject();
	}
	
	//###################################### sqlSessionFactory ######################################
	
	
	
	//###################################### sqlSessionTemplate ######################################
//	@Bean(name="sqlSessionMSR")
//    public SqlSessionTemplate sqlSessionMSR(@Qualifier("sqlSessionFactoryMSR") SqlSessionFactory sqlSessionFactoryMSR) {
//    	return new SqlSessionTemplate(sqlSessionFactoryMSR);
//    }
	
//	@Bean(name="sqlSessionHP")
//	public SqlSessionTemplate sqlSessionHP(@Qualifier("sqlSessionFactoryHP") SqlSessionFactory sqlSessionFactoryHP) {
//		return new SqlSessionTemplate(sqlSessionFactoryHP);
//	}
//	
//	@Bean(name="sqlSessionSALES")
//    public SqlSessionTemplate sqlSessionSALES(@Qualifier("sqlSessionFactorySALES") SqlSessionFactory sqlSessionFactorySALES) {
//    	return new SqlSessionTemplate(sqlSessionFactorySALES);
//    }
	
	@Bean(name="sqlSessionXO")
    public SqlSessionTemplate sqlSessionXO(@Qualifier("sqlSessionFactoryXO") SqlSessionFactory sqlSessionFactoryXO) {
    	return new SqlSessionTemplate(sqlSessionFactoryXO);
    }
	
//	@Bean(name="sqlSessionSCKSC")
//    public SqlSessionTemplate sqlSessionSCKSC(@Qualifier("sqlSessionFactorySCKSC") SqlSessionFactory sqlSessionFactorySCKSC) {
//    	return new SqlSessionTemplate(sqlSessionFactorySCKSC);
//    }
//	
	@Bean(name="sqlSessionOAUTH")
	public SqlSessionTemplate sqlSessionOAUTH(@Qualifier("sqlSessionFactoryOAUTH") SqlSessionFactory sqlSessionFactoryOAUTH) {
		return new SqlSessionTemplate(sqlSessionFactoryOAUTH);
	}
	//###################################### sqlSessionTemplate ######################################
	
	
	
	//###################################### TransactionManager ######################################
//	@Bean(name="transactionManagerMSR")
//	public PlatformTransactionManager transactionManagerMSR(@Qualifier("dataSourceMSR") DataSource dataSourceMSR) {
//		return new DataSourceTransactionManager(dataSourceMSR);
//	}
	
//	/**
//	 * ?ôà?éò?ù¥Ïß? TransactionManager
//	 * @param dataSourceHP
//	 * @return
//	 */
//	@Bean(name="transactionManagerHP")
//	public PlatformTransactionManager transactionManagerHP(@Qualifier("dataSourceHP") DataSource dataSourceHP) {
//		return new DataSourceTransactionManager(dataSourceHP);
//	}
	
	/**
	 * XO TransactionManager
	 * @param dataSourceMSR
	 * @return
	 */
	@Bean(name="transactionManagerXO")
	public PlatformTransactionManager transactionManagerXO(@Qualifier("dataSourceXO") DataSource dataSourceXO) {
		return new DataSourceTransactionManager(dataSourceXO);
	}
	
	/**
	 * XO-MSR TransactionManager
	 * @param dataSourceMSR
	 * @return
	 */
//	@Bean(name="transactionManagerXO_MSR")
//	public PlatformTransactionManager transactionManagerXO_MSR(@Qualifier("dataSourceXO") DataSource dataSourceXO, @Qualifier("dataSourceMSR") DataSource dataSourceMSR) {
//		return new ChainedTransactionManager (transactionManagerXO(dataSourceXO), transactionManagerMSR(dataSourceMSR));
//	}

//	/**
//	 * ?òÅ?óÖ?†ïÎ≥? TransactionManager
//	 * @param dataSourceSALES
//	 * @return
//	 */
//	@Bean(name="transactionManagerSALES")
//	public PlatformTransactionManager transactionManagerSALES(@Qualifier("dataSourceSALES") DataSource dataSourceSALES) {
//		return new DataSourceTransactionManager(dataSourceSALES);
//	}
//
//	/**
//	 * MSR-?ôà?éò?ù¥Ïß? TransactionManager
//	 * @param dataSourceMSR
//	 * @param dataSourceSALES
//	 * @return
//	 */
//	@Bean(name="transactionManagerMSR_HP")
//	public PlatformTransactionManager transactionManagerMSR_HP(@Qualifier("dataSourceMSR") DataSource dataSourceMSR, @Qualifier("dataSourceHP") DataSource dataSourceHP) {
//		return new ChainedTransactionManager(transactionManagerMSR(dataSourceMSR), transactionManagerHP(dataSourceHP));
//	}
	
//	/**
//	 * MSR-?òÅ?óÖ?†ïÎ≥? TransactionManager
//	 * @param dataSourceMSR
//	 * @param dataSourceSALES
//	 * @return
//	 */
//	@Bean(name="transactionManagerMSR_SALES")
//	public PlatformTransactionManager transactionManagerMSR_SALES(@Qualifier("dataSourceMSR") DataSource dataSourceMSR, @Qualifier("dataSourceSALES") DataSource dataSourceSALES) {
//		return new ChainedTransactionManager(transactionManagerMSR(dataSourceMSR), transactionManagerSALES(dataSourceSALES));
//	}
//	
//	/**
//	 * ?òÅ?óÖ?†ïÎ≥?(?ôòÎ∂àÏã†Ï≤??ö©) TransactionManager
//	 * @param dataSourceSALES
//	 * @return
//	 */
//	@Bean(name="transactionManagerSCKSC")
//	public PlatformTransactionManager transactionManagerSCKSC(@Qualifier("dataSourceSCKSC") DataSource dataSourceSCKSC) {
//		return new DataSourceTransactionManager(dataSourceSCKSC);
//	}
//	
//	/**
//	 * MSR-?òÅ?óÖ?†ïÎ≥?(?ôòÎ∂àÏã†Ï≤??ö©) TransactionManager
//	 * @param dataSourceMSR
//	 * @param dataSourceSALES
//	 * @return
//	 */
//	@Bean(name="transactionManagerMSR_SCKSC")
//	public PlatformTransactionManager transactionManagerMSR_SCKSC(@Qualifier("dataSourceMSR") DataSource dataSourceMSR, @Qualifier("dataSourceSCKSC") DataSource dataSourceSCKSC) {
//		return new ChainedTransactionManager(transactionManagerMSR(dataSourceMSR), transactionManagerSCKSC(dataSourceSCKSC));
//	}
	//###################################### TransactionManager ######################################
	
	
	
	//###################################### TransactionTemplate ######################################
//	@Bean(name="transactionTemplateMSR")
//	public TransactionTemplate transactionTemplateMSR(@Qualifier("transactionManagerMSR") PlatformTransactionManager transactionManagerMSR){
//		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManagerMSR);
//		return transactionTemplate;
//	}
	
	@Bean(name="transactionTemplateXO")
	public TransactionTemplate transactionTemplateXO(@Qualifier("transactionManagerXO") PlatformTransactionManager transactionManagerXO){
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManagerXO);
		return transactionTemplate;
	}
	
//	@Bean(name="transactionTemplateHP")
//	public TransactionTemplate transactionTemplateHP(@Qualifier("transactionManagerHP") PlatformTransactionManager transactionManagerHP){
//		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManagerHP);
//		return transactionTemplate;
//	}
//	
//	@Bean(name="transactionTemplateSALES")
//	public TransactionTemplate transactionTemplateSALES(@Qualifier("transactionManagerSALES") PlatformTransactionManager transactionManagerSALES){
//		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManagerSALES);
//		return transactionTemplate;
//	}
//	
//	@Bean(name="transactionTemplateSCKSC")
//	public TransactionTemplate transactionTemplateSCKSC(@Qualifier("transactionManagerSCKSC") PlatformTransactionManager transactionManagerSCKSC){
//		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManagerSCKSC);
//		return transactionTemplate;
//	}
	//###################################### TransactionTemplate ######################################
	
	
	
	/**
	 * MSR Mapper Scan
	 * package : co.kr.istarbucks.msr.**
	 * @return
	 */
//	@Bean
//	public MapperScannerConfigurer mapperScannerConfigurerMSR(){
//		MapperScannerConfigurer mc = new MapperScannerConfigurer();
//		mc.setNameGenerator(new StarbucksBeanNameGenerator());
//		mc.setBasePackage("co.kr.istarbucks.msr.**");
//		mc.setSqlSessionFactoryBeanName("sqlSessionFactoryMSR");
//		return mc;
//	}
	
//	/**
//	 * ?ôà?éò?ù¥Ïß? Mapper Scan
//	 * package : co.kr.istarbucks.hp.**
//	 * @return
//	 */
//	@Bean
//	public MapperScannerConfigurer mapperScannerConfigurerHP(){
//		MapperScannerConfigurer mc = new MapperScannerConfigurer();
//		mc.setNameGenerator(new StarbucksBeanNameGenerator());
//		mc.setBasePackage("co.kr.istarbucks.hp.**");
//		mc.setSqlSessionFactoryBeanName("sqlSessionFactoryHP");
//		return mc;
//	}
	
//	/**
//	 * ?òÅ?óÖ?†ïÎ≥? Mapper Scan
//	 * package : co.kr.istarbucks.sales.**
//	 * @return
//	 */
//	@Bean
//	public MapperScannerConfigurer mapperScannerConfigurerSALES(){
//		MapperScannerConfigurer mc = new MapperScannerConfigurer();
//		mc.setNameGenerator(new StarbucksBeanNameGenerator());
//		mc.setBasePackage("co.kr.istarbucks.sales.**");
//		mc.setSqlSessionFactoryBeanName("sqlSessionFactorySALES");
//		return mc;
//	}
	
//	/**
//	 * ?òÅ?óÖ?†ïÎ≥? Mapper Scan
//	 * package : co.kr.istarbucks.scksc.**
//	 * @return
//	 */
//	@Bean
//	public MapperScannerConfigurer mapperScannerConfigurerSCKSC(){
//		MapperScannerConfigurer mc = new MapperScannerConfigurer();
//		mc.setNameGenerator(new StarbucksBeanNameGenerator());
//		mc.setBasePackage("co.kr.istarbucks.scksc.**");
//		mc.setSqlSessionFactoryBeanName("sqlSessionFactorySCKSC");
//		return mc;
//	}
	
	/**
	 * ?Ç¨?ù¥?†å?ò§?çî Mapper Scan
	 * package : co.kr.istarbucks.xo.**
	 * @return
	 */
	@Bean
	public MapperScannerConfigurer mapperScannerConfigurerXO(){
		MapperScannerConfigurer mc = new MapperScannerConfigurer();
		mc.setNameGenerator(new StarbucksBeanNameGenerator());
		mc.setBasePackage("co.kr.istarbucks.xo.**");
		mc.setSqlSessionFactoryBeanName("sqlSessionFactoryXO");
		return mc;
	}

	@Bean
	public MapperScannerConfigurer mapperScannerConfigurerOAUTH(){
		MapperScannerConfigurer mc = new MapperScannerConfigurer();
		mc.setNameGenerator(new StarbucksBeanNameGenerator());
		mc.setBasePackage("co.kr.istarbucks.oauth.**");
		mc.setSqlSessionFactoryBeanName("sqlSessionFactoryOAUTH");
		return mc;
	}
	
	protected String getConfigLocation() {
		return "classpath:mybatis-config.xml";
	}

//	protected String getMapperLocationPatternMSR() {
//		return "classpath:co/kr/istarbucks/msr/**/*Mapper.xml";
//	}
	
//	protected String getMapperLocationPatternHP() {
//		return "classpath:co/kr/istarbucks/hp/**/*Mapper.xml";
//	}
//	
//	protected String getMapperLocationPatternSALES() {
//		return "classpath:co/kr/istarbucks/sales/**/*Mapper.xml";
//	}
//	
//	protected String getMapperLocationPatternSCKSC() {
//		return "classpath:co/kr/istarbucks/scksc/**/*Mapper.xml";
//	}
	
	protected String getMapperLocationPatternXO() {
		return "classpath:co/kr/istarbucks/xo/**/*Mapper.xml";
	}

	protected String getMapperLocationPatternOAUTH() {
		return "classpath:co/kr/istarbucks/oauth/**/*Mapper.xml";
	}
	
	protected Properties getConfigurationProperties() {
		return null;
	}
}
