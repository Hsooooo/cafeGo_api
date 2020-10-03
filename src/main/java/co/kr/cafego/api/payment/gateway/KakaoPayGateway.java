package co.kr.cafego.api.payment.gateway;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * 카카오페이 연동구간
 * @author Hsooooo
 *
 */
@Service
public class KakaoPayGateway {

	@Autowired
	private Environment env;
	
	private static final Logger logger = LoggerFactory.getLogger("KAKAO");
	
	
	/**
	 * <결제준비>에 해당하는 메서드
	 *  * 필수!!!!!!!!!!!!!!!!!!
	 *     - cartNo : 주문서 번호
	 *     - memberEmail : 회원 이메일
	 *     - cartName : 주문서 명
	 *     - totalAmt : 총 금액
	 * @param payMap
	 * @return tid
	 * @throws URISyntaxException 
	 */
	@SuppressWarnings("unchecked")
	public String kakaoReady(Map<String, Object> payMap) throws URISyntaxException {
		RestTemplate restTemplate 	  = new RestTemplate();
		Map<String, String> returnMap = new HashMap<String, String>();
		
		String tid = "";
		
		try {
			//===============================전달된 param 및 기본 설정=================================//
			String host			  = env.getProperty("kakao.pay.host", "https://kapi.kakao.com");
			String cid 			  = env.getProperty("kakao.pay.cid",  "TC0ONETIME");
			String adminKey		  = env.getProperty("kakao.adminKey", "8457b163a3812e915b0cf42d29645aff");
			String partnerOrderId = (String)payMap.get("cartNo");
			String partnerUserId  = (String)payMap.get("memberEmail");
			String itemName 	  = (String)payMap.get("cartName");
			
			//상품 수량
			int totalAmount    = (Integer)payMap.get("totalAmt");
			
			String approvalUrl = "http://127.0.0.1:8080/payment";		//성공시
			String cancelUrl   = "http://127.0.0.1:8080/payment";		//취소시
			String failUrl 	   = "http://127.0.0.1:8080/payment";		//실패시
			
			//========================================================================================//
			
			//===============================카카오페이 연동 정보 세팅================================//
			//--------------------------카카오페이 필수 파라미터 세팅---------------------------------//
			MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			params.add("cid", 			   cid);
			params.add("partner_order_id", partnerOrderId);
			params.add("partner_user_id",  partnerUserId);
			params.add("item_name",		   itemName);
			params.add("quantity",		   "1");
			params.add("total_amount", 	   Integer.toString(totalAmount));
			params.add("tax_free_amount",  "0");
			params.add("approval_url", 	   approvalUrl);
			params.add("cancel_url", 	   cancelUrl);
			params.add("fail_url", 		   failUrl);
			//--------------------------카카오페이 필수 파라미터 세팅---------------------------------//
			
			//-----------------------------------[HEADER]---------------------------------------------//
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "KakaoAK " + adminKey); //KakaoAK + adminKey
			headers.add("Content-type",  MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=utf-8");
			//-----------------------------------[HEADER]---------------------------------------------//
			
			//========================================================================================//
			
			HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String,String>>(params, headers);
			logger.info(body.toString());
			
			returnMap = restTemplate.postForObject(new URI(host + "/v1/payment/ready"), body, Map.class);
			logger.info(returnMap.toString());
			
			tid = returnMap.get("tid"); 
		} catch (RestClientException rce) {
			logger.error("RestClientException ->", rce);
			throw rce;
		} catch (URISyntaxException use) {
			logger.error("URISyntaxException ->", use);
			throw use;
		}
		return tid;
		
	}
	
	/**
	 * <결제승인>에 해당하는 메서드
	 * @param payMap
	 * @return <String, Object> returnMap
	 * @throws URISyntaxException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> kakaoApprove(Map<String, Object> payMap) throws URISyntaxException {
		RestTemplate restTemplate 	  = new RestTemplate();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		try {
			String host			  = env.getProperty("kakao.pay.host", "https://kapi.kakao.com");
			String cid 			  = env.getProperty("kakao.pay.cid",  "TC0ONETIME");
			String adminKey		  = env.getProperty("kakao.adminKey", "8457b163a3812e915b0cf42d29645aff");
			String partnerOrderId = (String)payMap.get("cartNo");
			String partnerUserId  = (String)payMap.get("memberEmail");
			String tid		 	  = (String)payMap.get("tid");
			String pgToken	 	  = (String)payMap.get("pgToken");
			
			//상품 수량
			int totalAmount    = (Integer)payMap.get("totalAmt");
			
			MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			
			//--------------------------카카오페이 필수 파라미터 세팅---------------------------------//
			params.add("cid", 			   cid);
			params.add("tid", 			   tid);
			params.add("partner_order_id", partnerOrderId);
			params.add("partner_user_id",  partnerUserId);
			params.add("pg_token",		   pgToken);
			params.add("total_amount", 	   Integer.toString(totalAmount));
			//--------------------------카카오페이 필수 파라미터 세팅---------------------------------//
			
			//-----------------------------------[HEADER]---------------------------------------------//
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "KakaoAK " + adminKey); //KakaoAK + adminKey
			headers.add("Content-type",  MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=utf-8");
			//-----------------------------------[HEADER]---------------------------------------------//
			
			HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String,String>>(params, headers);
			logger.info(body.toString());
			returnMap = restTemplate.postForObject(new URI(host + "/v1/payment/approve"), body, Map.class);
			logger.info(returnMap.toString());
			
		} catch (RestClientException rce) {
			logger.error("RestClientException ->", rce);
			throw rce;
		} catch (URISyntaxException use) {
			logger.error("URISyntaxException ->", use);
			throw use;
		}
		return returnMap;
	}
	
	
}
