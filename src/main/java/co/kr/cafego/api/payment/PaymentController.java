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
import co.kr.cafego.common.util.ResultCode;
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
	/ * @param model
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
			String memberNum  = (String) parameters.get("memberNum");
			String totalAmt	  = (String) parameters.get("totalAmt");
			String cartName	  = (String) parameters.get("cartName");
			String cartNo	  = (String) parameters.get("cartNo");
			String payMethod  = (String) parameters.get("payMethod");
			String pointUseYn = (String) parameters.get("pointUseYn");
			String pointAmt	  = (String) parameters.get("pointAmt");
			
			
			if(StringUtils.isBlank(memberNum) || StringUtils.isBlank(totalAmt) || StringUtils.isBlank(cartName)
					|| StringUtils.isBlank(cartNo) || StringUtils.isBlank(payMethod) || StringUtils.isBlank(pointUseYn)) {
				throw new ApiException(ResultCode.INVALID_PARAMETER, "필수 파라미터 NULL");
			}
			if(!StringUtils.isNumeric(totalAmt)) {
				throw new ApiException(ResultCode.INVALID_PARAMETER, "파라미터 형식이 맞지않음");
			}
			
			if(StringUtils.equals(pointUseYn, "Y")) {
				if(StringUtils.isBlank(pointAmt)) {
					throw new ApiException(ResultCode.INVALID_PARAMETER, "필수 파라미터 NULL");
				}
				if(!StringUtils.isNumeric(pointAmt)) {
					throw new ApiException(ResultCode.INVALID_PARAMETER, "파라미터 형식이 맞지않음");
				}
				if(Integer.parseInt(totalAmt) < Integer.parseInt(pointAmt)) {
					throw new ApiException(ResultCode.INVALID_PARAMETER, "지불 총액의 합이 포인트 사용액수보다 작음");
				}
			}
			
			logger.info("cartName" + cartName);
			
			paramMap.put("memberNum", memberNum);
			paramMap.put("totalAmt", totalAmt);
			paramMap.put("cartNo", cartNo);
			paramMap.put("cartName", cartName);
			paramMap.put("payMethod", payMethod);
			paramMap.put("pointAmt", pointAmt);
			
			obj = paymentService.payNOrder(paramMap);
			
			
			ro.getObject(request, headers, model, obj);
		}catch(ApiException ae) {
			logger.error("[{}][{}]","결제 및 주문 처리 오류", ae.getMessage());
			ro.setResult(response, ae.getResultCode());
		}catch(Exception e) {
			logger.error("Exception" , e);
			ro.setResult(response, ResultCode.SERVER_ERROR);
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
