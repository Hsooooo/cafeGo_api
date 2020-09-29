package co.kr.cafego.api.payment;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.kr.cafego.api.member.MemberService;
import co.kr.cafego.api.payment.gateway.KakaoPayGateway;
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
		
		
		obj = paymentService.payNOrder(paramMap);
		
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
