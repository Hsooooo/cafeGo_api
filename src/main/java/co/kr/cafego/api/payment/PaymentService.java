package co.kr.cafego.api.payment;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.cafego.api.member.dto.MemberPointInfoDto;
import co.kr.cafego.api.member.model.MemberBasicInfoModel;
import co.kr.cafego.common.exception.ApiException;
import co.kr.cafego.common.util.ResultCode;
import co.kr.cafego.core.support.ApiSupport;

/**
 * 6. 결제
 * @author Hsooooo
 *
 */
@Service
public class PaymentService extends ApiSupport{

	@Autowired
	private PaymentMapper paymentMapper;
	
	/**
	 * 6.1. 결제 수단 조회
	 * @param paramMap
	 * @return
	 */
	@Transactional(value="transactionManager", rollbackFor= {Exception.class, ApiException.class})
	public Object paymentList(Map<String, String> paramMap) {
		try {
			
			
		}catch(ApiException ae) {
			logger.error(ae.getMessage());
			throw ae;		// throw 하게되면 상단(Controller)에서도 Exception 처리를 하겠다 라는 의미
		}catch(Exception e) {
			throw e;
		}
		return null;
	}
	
	/**
	 * 6.2. 결제 및 주문
	 * [주문정보 유효성 체크] - [주문정보 저장] - [결제준비]
	 * @param paramMap
	 * @return
	 */
	@Transactional(value="transactionManager", rollbackFor= {Exception.class, ApiException.class})
	public Object payNOrder(Map<String, String> paramMap) {
		MemberBasicInfoModel model = new MemberBasicInfoModel();
		//Controller에서 전달된 값 변수 저장
		String memberName = paramMap.get("memberName");
		String memberSex  = paramMap.get("memberSex");
		String phone	  = paramMap.get("memberPhone");
		String email	  = paramMap.get("memberEmail");
		
		try {
			
		}catch(ApiException ae) {
			logger.error(ae.getMessage());
			throw ae;		// throw 하게되면 상단(Controller)에서도 Exception 처리를 하겠다 라는 의미
		}catch(Exception e) {
			throw e;
		}
		return model;
	}
	
	
	private boolean validateSample() {
		return false;
	}
}
