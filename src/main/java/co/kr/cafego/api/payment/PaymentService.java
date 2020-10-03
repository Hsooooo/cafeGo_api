package co.kr.cafego.api.payment;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.cafego.api.cart.dto.CartInfoDto;
import co.kr.cafego.api.member.MemberMapper;
import co.kr.cafego.api.member.dto.MemberPointInfoDto;
import co.kr.cafego.api.member.model.MemberBasicInfoModel;
import co.kr.cafego.api.payment.gateway.KakaoPayGateway;
import co.kr.cafego.common.exception.ApiException;
import co.kr.cafego.common.util.DataCode;
import co.kr.cafego.common.util.DateTime;
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
	
	@Autowired
	private KakaoPayGateway kakaoGate;
	
	@Autowired
	private MemberMapper memberMapper;
	
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
	@Transactional(value="transactionManager", rollbackFor= {Exception.class, ApiException.class, SQLException.class})
	public Object payNOrder(Map<String, String> paramMap) throws Exception{
		MemberBasicInfoModel model = new MemberBasicInfoModel();
		Map<String, Object> dbMap = new HashMap<String, Object>();
		Map<String, Object> payMap = new HashMap<String, Object>();
		
		//Controller에서 전달된 값 변수 저장
		String memberNum   = paramMap.get("memberNum");
		String totalAmt    = paramMap.get("totalAmt");
		String cartName	   = paramMap.get("cartName");
		String cartNo	   = paramMap.get("cartNo");
		String payMethod   = paramMap.get("payMethod");
		String pointAmt	   = paramMap.get("pointAmt");
		String memberEmail = "";
		try {
			String today = DateTime.getCurrentDate(1);		//yyyyMMddHHmm
			// step 1. 주문 가능한 회원 여부 판단
			dbMap.put("memberNum", memberNum);
			MemberPointInfoDto pointDto = memberMapper.getMemberPointInfo(dbMap);
			if(pointDto == null) {
				throw new ApiException(ResultCode.MEM_01, "조회된 회원 정보 없음");
			}
			if(StringUtils.isNotBlank(pointAmt)) {
				if(pointDto.getPoint() < Integer.parseInt(pointAmt)){
					throw new ApiException(ResultCode.PAY_01, "포인트 한도 초과");
				}
			}else {
				pointAmt = "0";
			}
			
			// step 2. 주문 가능한 카트 정보 여부 판단
			dbMap.clear();
			dbMap.put("cartNo", cartNo);
			CartInfoDto cartDto = paymentMapper.orderPossibleCartInfo(dbMap);
			
			if(cartDto == null) {
				throw new ApiException(ResultCode.PAY_02, "주문 가능한 카트 정보 아님");
			}
			
			// step 3. 주문 가능한 매장 및 좌석 여부 판단
			
			
			// step 4. 결제 진행 (payMethod별 분기처리)
			
			// --공통 파라미터
			payMap.put("cartNo", 	  cartNo);
			payMap.put("memberEmail", pointDto.getMemberEmail());
			payMap.put("cartName", 	  cartName);
			int payAmt = Integer.parseInt(totalAmt) - Integer.parseInt(pointAmt);
			payMap.put("totalAmt", 	  payAmt);
			
			
			//카카오페이 결제일 경우
			if(StringUtils.equals(payMethod, "K")){
				dbMap.clear();
				
				String tid = "";
				tid = kakaoGate.kakaoReady(payMap);
				
				//카트정보 (READY) 상태로 변경
				dbMap.put("cartNo", cartNo);
				int updCnt = paymentMapper.updateCartPayReady(dbMap);
				
				
				//결제정보 저장(READY로 시작)
				dbMap.put("memberNum",  memberNum);
				dbMap.put("storeCode",  "2001");
				dbMap.put("paymentAmt", payAmt);
				dbMap.put("tid", 		tid);
				dbMap.put("payMethod", 	payMethod);
				dbMap.put("today", 		today);
				dbMap.put("pointAmt", 	pointAmt);
				
				
				dbMap.put("payStatus", DataCode.PAY_STATUS_READY);		//결제 준비상태 (01)
				int insNum = paymentMapper.insertPaymentInfo(dbMap);
				
				//포인트 사용금액이 0원이 아닐 경우
				if(!StringUtils.equals(pointAmt, "0")) {
					dbMap.put("useType", 	DataCode.MEMBER_POINT_TYPE_USE);
					
					//회원 포인트 정보 변경 (카카오페이의 경우 결제 실패 및 취소일 경우 뺀 금액을 더해줘야함
					int c = paymentMapper.updateMemberPoint(dbMap);
					//회원 포인트 정보 변경 이력 (카카오페이의 경우 결제 실패 및 취소일 경우 뺀 금액을 더해줘야함
					int d = paymentMapper.insertMemberPointHist(dbMap);
				}
			//회원 카드로 결제	
			}else if(StringUtils.equals(payMethod, "M")) {
				
			}
			
			
		}catch(ApiException ae) {
			throw ae;
		}catch(Exception e) {
			throw e;
		}
		return model;
	}
	
	
	private boolean validateSample() {
		return false;
	}
}
