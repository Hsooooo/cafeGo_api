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
	
	
}
