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
	
	/** 카테고리 타입(01: 음료, 02: 푸드, 05: 원두)*/
	public static final String XO_CATE_TYPE_DRINK = "01";
	public static final String XO_CATE_TYPE_FOOD  = "02";
	public static final String XO_CATE_TYPE_BEANS = "05";
	
	
	/** 매장수령 타입(S: 매장, C: 차량)*/
	public static final String XO_RECEIVE_TYPE_STORE = "S";
	public static final String XO_RECEIVE_TYPE_CAR 	 = "C";
	
	
	/** 유입채널 코드(81: BIXBY, 84: 네이버)*/
	public static final String MODEL_TYPE_BIXBY = "81";
	public static final String MODEL_TYPE_NAVER = "84";
	
	
	/** RTD NEW CLASS CODE 시작 코드 */
	public static final String RTD_NEW_CLASS_CD_START = "1050";
	
	
	/** 할인타입 (C: SBC Extra D/C, T: Tumbler D/C, P: 페어링 D/C, S: 에코별, X: 사이렌 Extra D/C) */
	public static final String XO_DISC_TYPE_TUMBLER    = "T";
	public static final String XO_DISC_TYPE_FREEEXTRA  = "C";
	public static final String XO_DISC_TYPE_XFREEEXTRA = "X";
	public static final String XO_DISC_TYPE_PAIRING    = "P";
	public static final String XO_DISC_TYPE_ECHOSTAR   = "S";
	
	/** 결제타입 (E: 네이버테이블) */
	public static final String XO_PAYMENT_METHOD_NAVER = "E";
	
	// 주문 상태
	public static final String ORDER_PAY_REQ 		 = "10";
	public static final String ORDER_PAY_COMP 		 = "11";
	public static final String ORDER_PAY_CANCEL 	 = "12";
	public static final String ORDER_PAY_CANCEL_FAIL = "13";
	public static final String ORDER_PAY_PARTFAIL 	 = "14";
	public static final String ORDER_PAY_FAIL 		 = "15";
	public static final String ORDER_PAY_CANCEL_PROC = "16";
	public static final String ORDER_PAY_WAITING 	 = "17";
	public static final String ORDER_ORDER_REQ 		 = "20";
	public static final String ORDER_ORDER_CANCEL 	 = "22";
	public static final String ORDER_ORDER_DONE 	 = "31";
	public static final String ORDER_PICKUP_DONE 	 = "32";
	
	//결제 종류
	//1 - APP, 2 - POS
	public static final String PAY_KIND_APP			 = "1";
	
	//MSR회원 구분
	//B - 비회원
	public static final String MSR_USER_FLAG_NOMEM   = "B";
	
	//매장지정타입
	public static final String STORE_SEL_TYPE_ETC   = "4";
	
	//결제 결과코드
	//성공
	public static final String PAY_RES_CODE_SUCCESS	 = "00";
	
	//결제 결과 메세지
	public static final String PAY_RES_MSG_SUCCESS	 = "결제 성공"; 
	
	//결제 상태
	//성공
	public static final String PAY_SUCCESS_STS		 = "P";
	
	public static final int ORDER_PAY_CANCEL_INT 	 = 21;
	public static final int ORDER_ORDER_CANCEL_INT 	 = 22;
	public static final int ORDER_ORDER_DONE_INT 	 = 31;
	public static final int ORDER_PICKUP_DONE_INT 	 = 32;
	
	/** 컵타입(머그)*/
	public static final String CUP_TYPE_MUG 		 = "0";
	/** 컵타입(일회용)*/
	public static final String CUP_TYPE_ONECUP 		 = "1";
	/** 컵타입(개인컵)*/
	public static final String CUP_TYPE_TUMBLER 	 = "2";
	
	//텀블러 리워드 타입 - 비회원
	public static final String XO_TUMBL_REWARD_TYPE_NNM = "D";
	
	//에코 보너스타입
	//S - 별 , D - 할인
	public static final String ECO_BUNUS_TYPE_DISC	 = "D";
	
	//비회원 객층코드 - 11
	public static final String NONMEMBER_GSTDGR_CODE = "11";
	
	/** 포장 여부 : Y(전체 포장)*/
	public static final String PACKING_FLAG_Y		 = "Y";
	/** 포장 여부 : N(전체 미포장)*/
	public static final String PACKING_FLAG_N		 = "N";
	/** 포장 여부 : F(푸드만 포장)*/
	public static final String PACKING_FLAG_F		 = "F";
	
	/** 테이크아웃 여부 : Y*/
	public static final String TAKEOUT_FLAG_Y		 = "Y";
	/** 테이크아웃 여부 : N*/
	public static final String TAKEOUT_FLAG_N		 = "N";
	
	/** os Type code ( 9: 기타)*/
	public static final String OS_TYPE_STR_ETC		 = "9";
	
	/** 상품유형 - 매장*/
	public static final String BIZTYPE_VISIT	 	 = "VISIT";
	/** 상품유형 - 포장*/
	public static final String BIZTYPE_PICKUP		 = "PICKUP";
	/** 상품유형 - DT*/
	public static final String BIZTYPE_DRIVETHRU	 = "DRIVETHRU";
	
	
	/** 객층코드 (22 : 기타) */
	public static final String CUST_CODE_ETC		 = "22";
	
	/** 네이버테이블:주문승인 */
	public static final String NAVER_STS_ORD_PREPARING 	= "ORDER_PREPARING";
	/** 네이버테이블:제조완료 */
	public static final String NAVER_STS_ORD_PREPARED 	= "ORDER_PREPARED";
	/** 네이버테이블:픽업완료 */
	public static final String NAVER_STS_COMPLETED 		= "COMPLETED";
	/** 네이버테이블:주문취소 */
	public static final String NAVER_STS_CANCELLED 		= "CANCELLED";
	/** 네이버테이블 주문처리(주문서검증)*/
	public static final String NAVER_ORDER_STS_VERIFY	= "pre-order-requested";
	/** 네이버테이블 주문처리(주문처리)*/
	public static final String NAVER_ORDER_STS_CONFIRM	= "confirmed";
	
	
	/** 네이버주문서비스 유저 이름*/
	public static final String NAVER_USER_NAME			= "네이버주문";
	
}
