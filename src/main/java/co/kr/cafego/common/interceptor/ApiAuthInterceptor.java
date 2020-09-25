/*
 * @(#) $Id: ApiAuthInterceptor.java,v 1.4 2018/03/22 04:46:47 iamjihun Exp $
 * 
 * Starbucks Service
 * 
 * Copyright 2015 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, Digitalro 306, Guro-gu,
 * Seoul, Korea
 */

/**
 * 
 */
package co.kr.cafego.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import co.kr.istarbucks.common.exception.ApiAuthException;
import co.kr.istarbucks.common.util.ResultCode;
import co.kr.istarbucks.core.config.SystemEnviroment;

/**
 * TODO Insert type comment for ApiAuthInterceptor.
 *
 * @author sw.Lee
 * @version $Revision: 1.4 $
 */
public class ApiAuthInterceptor extends HandlerInterceptorAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger("INFO");
	
	private Environment env;
	public ApiAuthInterceptor() {}
	public ApiAuthInterceptor(Environment environment){
		this.env = environment;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		//======================================================================================
		// 1. 서버 작업 여부 체크 => Y 이면 모든 전문은 응답코드 010 처리 
		//======================================================================================
		String serverWorking = env.getProperty("server.work","N");
		if("Y".equals(serverWorking)) {
			throw new ApiAuthException(ResultCode.SERVER_WORKING, "서버 작업 중 입니다.");
		}
		
		//======================================================================================
		// 2. Request Method 체크 => EXT 인터페이스는 모두 POST, GET, PATCH, DELETE 메소드만 사용
		//======================================================================================

		if(isRequiredMethod(request)) {
			throw new ApiAuthException(ResultCode.INVALID_METHOD, "전문요청 메소드는 POST, GET, PATCH, DELETE 방식만 지원합니다.");
		}

		//======================================================================================
		// 3. Protocol 체크 => APP 인터페이스는 모두 HTTPS 프로토콜을 사용
		//======================================================================================
		LOGGER.info("Api Operation Mode ::: " + SystemEnviroment.getActiveProfile().replaceAll("\n|\r", ""));
		if(isRealModeAndNotSecured(request)) {
			throw new ApiAuthException(ResultCode.INVALID_PROTOCOL, "전문요청 프로토콜은 HTTPS만 지원합니다.");
		}
		return super.preHandle(request, response, handler);
	}

	private boolean isRealModeAndNotSecured(HttpServletRequest request) {
		return SystemEnviroment.getActiveProfile().equals("prod") && !request.isSecure();
	}

	private boolean isRequiredMethod(HttpServletRequest request) {
		return !"POST".equalsIgnoreCase(request.getMethod()) && !"PATCH".equalsIgnoreCase(request.getMethod()) 
				&& !"GET".equalsIgnoreCase(request.getMethod()) && !"DELETE".equalsIgnoreCase(request.getMethod());
	}
	public Environment getEnv() { return env; }
	public void setEnv(Environment env) { this.env = env; }
	
}
