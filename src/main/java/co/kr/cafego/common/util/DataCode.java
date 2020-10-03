package co.kr.cafego.common.util;

public final class DataCode {

	/**
	 * getCodeName
	 * 예) DataCode.getCodeName(DataCode.PAY_METHOD_ARR, "S")
	 * @param arr 배열
	 * @param code
	 * @return CodeName
	 */
	public static String getCodeName ( String[][] selectArr, String code ) {
		String codeName = "";
		
		if ( code == null ) return codeName;
		if ( selectArr == null ) return code;
		
		for ( String[] arr : selectArr ) {
			if ( code.equals (arr[0]) ) {
				codeName = arr[1];
				break;
			}
		}
		
		if ( "".equals (codeName) ) codeName = code;
		
		return codeName;
	}
	
	/**
	 * getCodeNameDef
	 * 예) DataCode.getCodeName(DataCode.PAY_METHOD_ARR, "S")
	 * @param arr 배열
	 * @param code
	 * @return CodeName
	 */
	public static String getCodeNameDef ( String[][] selectArr, String code, String dif ) {
		String codeName = "";
		
		if ( code == null ) return codeName;
		if ( selectArr == null ) return code;
		
		for ( String[] arr : selectArr ) {
			if ( code.equals (arr[0]) ) {
				codeName = arr[1];
				break;
			}
		}
		
		if ( "".equals (codeName) ) codeName = dif;
		
		return codeName;
	}
	
	// 주문취소 코드 (사용)
	public static final String[][] ORDER_CANCEL_ARR = { {"01", "재고없음"}
													  , {"02", "원부재료 소진"}
													  , {"99", "기타"}
													  , {"03", "타 매장 주문"}
													  , {"05", "기타"}
													  , {"06", "재고없음"}
													  , {"07", "대기시간 불만"}
													  , {"08", "결제수단 변경"}
													  };
	
	// 단말 구분
	public static final String[][] OS_TYPE_CODE_ARR = { {"IOS", "1"}, {"ANDROID", "2"}, {"기타", "9"} };
	
	
	// 회원 포인트 사용 구분코드
	
	// U : 사용, A : 적립, J : 회원 가입기념 포인트, C : 카드 등록 기념 포인트, R : 결제 실패 및 기타 사유로 인한 롤백
	public static final String MEMBER_POINT_TYPE_USE = "U";
	
	public static final String MEMBER_POINT_TYPE_JOIN = "J";
	
	public static final String MEMBER_POINT_TYPE_CARD = "C";
	
	public static final String MEMBER_POINT_TYPE_ADD = "A";
	
	public static final String MEMBER_POINT_TYPE_ROLLBACK = "R";
	
	
	//주문서 상태값
	//01 : 주문 이전, 02 : 주문 중, 03 : 주문 완료
	public static final String CART_STATUS_ORDER_BEFORE = "01";
	
	public static final String CART_STATUS_ORDER_ING = "02";
	
	public static final String CART_STATUS_ORDER_AFTER = "03";
	
	//결제 정보 상태값
	//01 : 결제 준비, 02 : 결제 실패, 00 : 결제 성공
	public static final String PAY_STATUS_READY = "01";
	
	public static final String PAY_STATUS_FAIL = "02";
	
	public static final String PAY_STATUS_SUCCESS = "00";
	
	
	/**   관리자 로그인 	 */
	// Y : 관리자 정보 있으며 로그인 성공, N : 관리자 정보 없으며 로그인 성공
	public static final String LOGIN_SUCESS = "Y";
	
	public static final String LOGIN_FAIL = "N";
	
}
