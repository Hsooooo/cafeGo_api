package co.kr.cafego.api.payment;

import java.util.Map;

import co.kr.cafego.api.cart.dto.CartInfoDto;

public interface PaymentMapper {
	
	/**
	 * 결제 준비(장바구니 상태 변경)
	 * @param dbMap
	 * @return
	 */
	public int updateCartPayReady(Map<String, Object> dbMap);
	
	/**
	 * 결제 정보 저장
	 * @param dbMap
	 * @return
	 */
	public int insertPaymentInfo(Map<String, Object> dbMap);

	/**
	 * 결제 가능한 주문서 정보 조회
	 * @param dbMap
	 * @return
	 */
	public CartInfoDto orderPossibleCartInfo(Map<String, Object> dbMap);

	/**
	 * 회원 포인트 변경
	 * @param dbMap
	 * @return
	 */
	public int updateMemberPoint(Map<String, Object> dbMap);

	/**
	 * 포인트 변경 내역 저장
	 * @param dbMap
	 * @return
	 */
	public int insertMemberPointHist(Map<String, Object> dbMap);
}
