/*
 * @(#) $Id: DefaultControllerAdvice.java,v 1.2 2017/10/12 01:28:44 namgu1 Exp $
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
package co.kr.cafego.common.advice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import co.kr.istarbucks.common.exception.ApiAuthException;
import co.kr.istarbucks.common.exception.TokenException;
import co.kr.istarbucks.common.util.ReturnObject;

/**
 * TODO Insert type comment for DefaultControllerAdvice.
 *
 * @author sw.Lee
 * @version $Revision: 1.2 $
 */
@ControllerAdvice
@RestController
public class DefaultControllerAdvice {
	private static final Logger logger = LoggerFactory.getLogger("INFO");
	
	@Autowired
	private ReturnObject ro;
	
	
	//인증관련 Exception 처리
	@ExceptionHandler(ApiAuthException.class)
	public Object apiAuthException(HttpServletRequest request, HttpServletResponse response, ApiAuthException apiAuthException){
		logger.info("API 인증실패 : " + apiAuthException.getResultCode().replaceAll("\n|\r", "") + "/" + apiAuthException.getResultMessage().replaceAll("\n|\r", ""));
		
		ro.setResult(response, apiAuthException.getResultCode());
		return null;
	}
	
	//토큰체크 Exception 처리
	@ExceptionHandler(TokenException.class)
	public Object handleBusinessException(HttpServletResponse response, TokenException tokenException) {
		logger.info("[토큰 체크] 실패 : " + tokenException.getResultCode().replaceAll("\n|\r", "") + " " + tokenException.getResultMessage().replaceAll("\n|\r", ""));
		
		ro.setResult(response, tokenException.getResultCode());
		return null;
	}
}
