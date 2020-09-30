package co.kr.cafego.api.payment;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.kr.cafego.api.payment.gateway.KakaoPayGateway;
import co.kr.cafego.common.exception.ApiException;
import co.kr.cafego.common.util.ReturnObject;
import co.kr.cafego.core.support.ApiSupport;

/**
 * 6. 결제
 * @author Hsooooo
 *
 */
@RestController
@RequestMapping("/payment")
public class PaymentController extends ApiSupport {
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private KakaoPayGateway kakaoGate;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private ReturnObject ro;
	
	
	/**
	 * 6.1. 결제 수단 조회
	 * @param headers
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/paymentList.do", method=RequestMethod.POST)
	public Object paymentList(@RequestHeader HttpHeaders headers, HttpServletRequest request
			, HttpServletResponse response, Model model) {
		
		Object obj = null;
		Map<String, String> paramMap = new HashMap<String, String>();
		
		
		obj = paymentService.paymentList(paramMap);
		
		return obj;
		
	}
	
	/**
	 * 6.2. 결제 및 주문
	 * @param headers
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/payNOrder.do", method=RequestMethod.POST)
	public Object payNOrder(@RequestHeader HttpHeaders headers, HttpServletRequest request
			, HttpServletResponse response, Model model) {
		
		Object obj = null;
		Map<String, String> paramMap = new HashMap<String, String>();
		
		@SuppressWarnings("unchecked")
		Map<String, Object> parameters = (Map<String, Object>) request.getAttribute("bodyMap");
		
		try {
			String memberEmail = (String)parameters.get("memberEmail");
			int totalAmt	   = (int)(double) parameters.get("totalAmt");
			String cartName	   = (String) parameters.get("cartName");
			
			
			logger.info("cartName" + cartName);
			
			obj = paymentService.payNOrder(paramMap);
		}catch(ApiException ae) {
			
		}
		
		
		
		
		return obj;
		
	}
	
	/**
	 * 6.#. 카카오페이 결제 성공시
	 *  -> 결제 준비시 전달한 approval_url로 요청 받음
	 * @param headers
	 * @param request
	 * @param response
	 * @param model
	 * @param pgToken
	 * @return
	 */
	@RequestMapping(value="/kPaySuccess.do", method=RequestMethod.GET)
	public Object kPaySuccess(@RequestHeader HttpHeaders headers, HttpServletRequest request
			, HttpServletResponse response, Model model, @RequestParam("pg_token") String pgToken) {
		Object obj = null;
		try {
			if(StringUtils.isEmpty(pgToken)) {
				throw new ApiException("pgToken NULL");
			}
			
			
			
			
		}catch(ApiException ae) {
			
		}
		
		return obj;
	}
	
	
	@RequestMapping(value="/kakaoTest.do", method=RequestMethod.POST)
	public Object kakaoTest(@RequestHeader HttpHeaders headers, HttpServletRequest request
			, HttpServletResponse response, Model model) {
		
		HashMap<String, Object> payMap = new HashMap<String, Object>();
		payMap.put("cartNo","123456");
		payMap.put("memberEmail","rkrk6469");
		payMap.put("cartName","카페아메리카노 외");
		payMap.put("totalAmt",22000);
		
		logger.info("################ envTest######" + env.getProperty("kakao.pay.host"));
		try {
			logger.info("############### tid #####" + kakaoGate.kakaoReady(payMap));
		}catch (Exception e) {
			logger.error("Error", e);
		}
		
		
		return null;
	}
}
