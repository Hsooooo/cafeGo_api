package co.kr.cafego.common.util;

public class ResultCode {
	/** 응답코드 : [000] 정상 처리되었습니다. */
	public static final String SUCCESS           = "000";
	
	/** 응답코드 : [001] 액세스 토큰이 유효하지 않습니다. */
	public static final String NO_TOKEN          = "001";
	
	/** 응답코드 : [002] 액세스 토큰의 기간이 만료되었습니다. */
	public static final String NO_SIRN_TOKEN     = "002";
	
	/** 응답코드 : [003] 요청 파라미터의 값이 잘못되었습니다. */
	public static final String INVALID_PARAMETER = "003";
	
	/** 응답코드 : [004] 잘못된 URL입니다. */
	public static final String INVALID_URL       = "004";
	
	/** 응답코드 : [005] 잘못된 요청입니다. */
	public static final String INVALID_REQUEST   = "005";
	
	/** 응답코드 : [006] 프로토콜 오류입니다. (HTTPS) */
	public static final String INVALID_PROTOCOL  = "006";
	
	/** 응답코드 : [007] 허용되지 않은 HTTP 메소드 입니다. (허용 메소드 : POST, GET, DELETE, PATCH) */
	public static final String INVALID_METHOD    = "007";
	
	/** 응답코드 : [008] DB 오류입니다. */
	public static final String DB_ERROR          = "008";
	
	/** 응답코드 : [009] 조회된 데이터가 없습니다. */
	public static final String NO_DATA           = "009";

	/** 응답코드 : [010] 서버 작업 중 입니다. */
	public static final String SERVER_WORKING    = "010";
	
	/** 응답코드 : [011] json data가 유효하지 않습니다. */
	public static final String JSON_DATA_ERROR   = "011";
	
	/** 응답코드 : [099] 기타 오류입니다. */
	public static final String SERVER_ERROR      = "099";
	

	// 네이버
	/** Message : 유효하지 않은 옵션 정보입니다. */
    public static final String EXT_01   = "101";
     
    /** Message : 주문 시 최대 {0}개 메뉴까지 가능합니다.\n수량을 먼저 조정해주세요. */
	public static final String EXT_02	= "102";
	
	/** Message : 주문 당 최대 메뉴 개수({0}개)를 초과 하였습니다. */
	public static final String EXT_03	= "103";
	
	/** Message : 주문정보 등록에 실패하였습니다. */
	public static final String EXT_04   = "104";
	
	/** Message : 주문 요청 정보가 잘못 되었습니다. 고객센터로 연락해주세요. */
	public static final String EXT_05	= "105";
	
	/** Message : 주문 정보를 찾을 수 없습니다. */
	public static final String EXT_06	= "106";
	
	/** Message : 한번에 최대 {0}개 메뉴까지만 주문하실 수 있어요. */
	public static final String EXT_07	= "107";
	
	/** Message : {0}점 주문가능 시간은 ({1}~{2})입니다. 이용시간 내 주문을 다시 시도해주세요. */
	public static final String EXT_08	= "108";
	
	/** Message : 오늘은 {0}점 휴점일 입니다. 다른 매장을 선택해주세요. */
	public static final String EXT_09	= "109";
	
	/** Message : 단종된 메뉴가 포함되어 있어요. */
	public static final String EXT_10	= "110";
	
	/** Message : {0}점에서 주문할 수 없는 메뉴가 포함되어 있습니다. */
	public static final String EXT_11	= "111";
	
	/** Message : 할인 정보에 문제가 있습니다. 고객센터로 연락해주세요. */
	public static final String EXT_12	= "112";
	
	/** Message : 결제 금액이 잘못되었습니다. 고객센터로 연락해주세요. */
	public static final String EXT_13	= "113";
	
	/** Message : 주문 정보를 저장할 수 없습니다. 고객센터로 연락해주세요. */
	public static final String EXT_14	= "114";
	
	/** Message : 잘못된 주문 정보입니다. */
	public static final String EXT_15	= "115";
	
	/** Message : 죄송합니다. 정상적으로 주문이 전송되지 않았습니다. 스타벅스 App의 히스토리 메뉴를 통해 다시 주문을 전송해 주세요. */
	public static final String EXT_16	= "116";
	
	/** Message : 담기는 최대 {0}개 메뉴까지 가능합니다.\n수량을 먼저 조정해주세요. */
	public static final String EXT_17  = "117";
	
	/** Message : 입력 메뉴의 대표 메뉴가 존재하지 않습니다. */
    public static final String EXT_18  = "118";
     
    /** Message : 결제 취소된 주문. */
    public static final String EXT_19  = "119";

    /** Message : 결제취소 불가능 주문(크리티컬 에러 존재) */
    public static final String EXT_20  = "120";
     
    /** Message : 주문정보 불일치 */
    public static final String EXT_21  = "121";
     
    /** Message : 품절된 메뉴가 포함되어 있습니다. [{0}] */
    public static final String EXT_22  = "122";
     
    /** Message : 조회된 대표메뉴 혹은 매장이 존재하지 않습니다. */
    public static final String EXT_23  = "123";
     
    /** Message : 해당 매장은 사이렌오더 불가능 매장입니다.*/
    public static final String EXT_24  = "124";
    
    /** Message : 허용되지 않은 POS입니다. */
    public static final String EXT_25  = "125";
}