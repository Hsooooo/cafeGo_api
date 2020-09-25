package co.kr.cafego.core.config;

import java.security.Security;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.number.NumberFormatter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import co.kr.istarbucks.BasePackageMarker;
import co.kr.istarbucks.common.interceptor.ApiAuthInterceptor;
import co.kr.istarbucks.common.interceptor.ReqJsonMappingInterceptor;
import co.kr.istarbucks.common.interceptor.TokenKeyCheckInterceptor;
import co.kr.istarbucks.common.util.MessageUtil;
import co.kr.istarbucks.common.util.ReturnObject;
import co.kr.istarbucks.core.support.CustomDateFormatter;
import co.kr.istarbucks.core.support.XssConverter;
import co.kr.istarbucks.oauth.token.HpOauthMapper;
import co.kr.istarbucks.xo.auth.AuthMapper;
import net.ezens.common.env.EnvironmentWrapper;
import net.ezens.common.trace.TraceConfig;
import net.ezens.common.web.config.AbstractApplicationConfig;


/**
 * Spring ?ôòÍ≤? ?Ñ§?†ï
 * @author Íπ?Í∑úÎÇ®
 */
@Configuration
@PropertySource({
	"classpath:${spring.profiles.active:dev}/default.properties",
	"classpath:messages.properties",
	"classpath:${spring.profiles.active:dev}/xoDefault.properties"
})
@Import({
	TraceConfig.class,
})
@ImportResource({
	"classpath:etc-config.xml",
})
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackageClasses = {BasePackageMarker.class}, nameGenerator = StarbucksBeanNameGenerator.class)
public class ApplicationConfig extends AbstractApplicationConfig {
	 
	private final Logger logger = LoggerFactory.getLogger("INFO");
	
	// properties ?öç?ìù
	@Inject
	private Environment env;
	
	@Inject
	private EnvironmentWrapper envw;
	
	@Inject
	private AuthMapper authMapper;
	
	@Inject
	private HpOauthMapper hpOauthMapper;
	
	public ApplicationConfig() {
		logger.info("{} is initializing.", ApplicationConfig.class);
	}

	@PostConstruct
	public void postConstruct() {
		SystemEnviroment.setActiveProfile(env.getActiveProfiles()[0]);
		//DH keypair Error fix
		Security.addProvider(new BouncyCastleProvider());
	}
	/**************************************** Bean definition ?ãú?ûë ****************************************/
	@Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
	
	@Bean(name="multipartResolver")
	public MultipartResolver multipartResolver() {//throws IOException {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		return multipartResolver;
	}
	
	//json Ï≤òÎ¶¨?ö© view
	@Bean
	public MappingJackson2JsonView jsonView(){
		MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
		jsonView.setModelKey("app");
		jsonView.setPrettyPrint(getJsonPrettyPrint());
		return jsonView;
	}
	
	//jsp Ï≤òÎ¶¨?ö© view
	@Bean
	public InternalResourceViewResolver viewResolver(){
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;		
	}
	
	@Bean
	public ReturnObject returnObject(){
		return new ReturnObject();
	}
	
	//Message.properties Ï≤òÎ¶¨?ö© util
	@Bean(name="messageUtil")
	public MessageUtil messageUtil(){
		MessageUtil messageUtil =  new MessageUtil(env);
		return messageUtil;
	}
	/**************************************** Bean definition ?Åù ****************************************/
	
	/****************************** WebMvcConfigurerAdapter overriding ?ãú?ûë ******************************/
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		PageableHandlerMethodArgumentResolver pageableArgumentResolver = new PageableHandlerMethodArgumentResolver();
		pageableArgumentResolver.setFallbackPageable(new PageRequest(1, 10));
		pageableArgumentResolver.setMaxPageSize(100);
		argumentResolvers.add(pageableArgumentResolver);
	}
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload_files/").addResourceLocations("/upload_files/**");
        registry.addResourceHandler("/css/").addResourceLocations("/css/**");
        registry.addResourceHandler("/images/").addResourceLocations("/images/**");
        registry.addResourceHandler("/js/").addResourceLocations("/js/**");
        registry.addResourceHandler("/resources/").addResourceLocations("/resources/**");
    }

	/**
	 * Interceptor
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new ApiAuthInterceptor(env));			//api ?ù∏Ï¶ùÍ??†® Ï≤¥ÌÅ¨
		registry.addInterceptor(new ReqJsonMappingInterceptor(env));	//json object mapping
		registry.addInterceptor(new TokenKeyCheckInterceptor(env, authMapper, hpOauthMapper));
	}
	
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatter(new CustomDateFormatter("yyyy-MM-dd"));
		registry.addFormatter(new NumberFormatter());
		registry.addConverter(new XssConverter());
	}
	/****************************** WebMvcConfigurerAdapter overriding ?Åù ******************************/
	
	//LocalValidatorFactoryBean?? ?Å¥?ûò?ä§?å®?ä§ ?Ç¥?óê JSR-303 Íµ¨ÌòÑÏ≤¥Ï? Í¥??†®?êú ?ùº?ù¥Î∏åÎü¨Î¶¨Î?? Í≤??Éâ?ïò?ó¨ ValidatorÎ•? ?ûê?èô?úºÎ°? Í≤??Éâ?ï¥Ï£ºÎäî ?ó≠?ï†?ùÑ ?àò?ñâ?ïú?ã§.
	@Bean
	public LocalValidatorFactoryBean localValidatorFactoryBean() {
		return new LocalValidatorFactoryBean();
	}
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor(){
		return new MethodValidationPostProcessor();
	}
	@Override
	protected boolean getJsonPrettyPrint() {
		return envw.getProperty("json.prettyPrint", Boolean.class, Boolean.TRUE);
	}
}
