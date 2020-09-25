package co.kr.cafego.common.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.env.Environment;

public class AppFunction {
	
	private final Environment dConf;
	
	public AppFunction (Environment conf) {
		this.dConf = conf;
	}
	
	/**
	 * 결과값에 한글 등 문자열의 결과값 리턴 할 때 CDATA 처리
	 * @param value
	 * @return
	 */
	public String wrapCdata ( String value ) {
		String returnStr = "";
		if ( StringUtils.isEmpty (value) ) {
			returnStr = "";
		} else {
			returnStr = "<![CDATA[" + value + "]]>";
		}
		return returnStr;
	}
	
	/**
	 * 주문 상대에 따른 버튼 출력 여부 확인
	 * @param type
	 * @param checkStr
	 * @return
	 */
	public String buttonFlag ( String type, String checkStr ) {
		
		String returnStr = "N";
		
		String checkStrs = this.dConf.getProperty("xo.button." + type);
		
		if ( StringUtils.isNotEmpty (checkStrs) ) {
			String[] strArr = checkStrs.split (";");
			for ( String targetStr : strArr ) {
				if (StringUtils.equals (targetStr, checkStr) ) {
					returnStr = "Y";
					break;
				}								
			}
		}
		return returnStr;
	}
	
	/**
	 * 배열 확인
	 * @param Object value
	 * @return boolean
	 */
	public boolean listCheck ( Object value ) {
		boolean result = false;
		if ( value instanceof List ) {
			result = true;
		}
		return result;
	}
	
	/**
	 * 객층코드 계산
	 * @param Object value
	 * @return boolean
	 */
	public static String calGstdgr ( String birthYear, String gender ) {
		String gstdgr = "";
		
		if ( StringUtils.isEmpty (birthYear) || StringUtils.isEmpty (gender) ) { return "11"; }
		
		try {
			Calendar cal = Calendar.getInstance ();
			int currentYear = cal.get (Calendar.YEAR);
			
			int age = currentYear - Integer.parseInt (birthYear) + 1;
			int generation = age / 10;
			if ( generation < 2 ) {
				generation = 1;
			} else if ( generation > 5 ) {
				generation = 5;
			}
			
			gstdgr = ( StringUtils.equals (gender, "M") ? "1" : "2" ) + generation;
		} catch ( Exception e ) {
			return "11";
		}
		
		return gstdgr;
	}
	
	/**
	 * AP ID를 매장코드로 변환
	 * 
	 * @param apId
	 * @return
	 */
	public static String convertStoreCd ( String apId ) {
		if ( StringUtils.isEmpty (apId) ) {
			return "";
		} else if ( apId.length () != 6 ) { return ""; }
		
		try {
			String id1 = apId.substring (0, 1);
			String id2 = apId.substring (1, 2);
			String id3 = apId.substring (2, 3);
			String id4 = apId.substring (3, 4);
			String id5 = apId.substring (4, 5);
			String id6 = apId.substring (5, 6);
			
			// id2를 2로 나눈 나머지가 id3과 같아야 함.
			int mod = Integer.parseInt (id2) % 2;
			
			if ( !String.valueOf (mod).equals (id3) ) { return ""; }
			
			// id5를 2로 나눈 나머지가 id6과 같아야 함.
			int mod2 = Integer.parseInt (id5) % 2;
			
			if ( !String.valueOf (mod2).equals (id6) ) { return ""; }
			
			return id2 + id1 + id5 + id4;
		} catch ( Exception e ) {
			return "";
		}
	}
	
	
	/**
	 * 주문번호 확인
	 * 
	 * @param apId
	 * @return
	 */
	public static boolean checkOrderNo ( String orderNo, String checkVal ) {
		if ( StringUtils.isEmpty (orderNo) || StringUtils.isEmpty (checkVal) ) { return false; }
		if ( !StringUtils.isNumeric (orderNo) || !StringUtils.isNumeric (checkVal) ) { return false; }
		if ( orderNo.length () != 20 ) { return false; }
		
		long numA = Integer.parseInt (StringUtils.substring (orderNo, 0, 5));
		long numB = Integer.parseInt (StringUtils.substring (orderNo, 5, 10));
		long numC = Integer.parseInt (StringUtils.substring (orderNo, 10, 15));
		long numD = Integer.parseInt (StringUtils.substring (orderNo, 15, 20));
		
		long numAD = numA * numD % 1000;
		long numBC = numB * numC % 1000;
		
		String sumValue = String.valueOf (numAD + numBC);
		
		int sum = 0;
		for ( int i = 0; i < sumValue.length (); i++ ) {
			sum += Integer.parseInt (StringUtils.substring (sumValue, i, i + 1));
		}
		
		StringBuffer sbSumValue = new StringBuffer();
		sbSumValue.append(sumValue).append(sum % 2);
		
		return StringUtils.equals (sbSumValue.toString(), checkVal);
	}
	
	/**
	 * orderNo 검증용 val 생성
	 * @param orderNo
	 * @return
	 */
//	private static String getCheckOrderVal(String orderNo){
//		if ( StringUtils.isEmpty (orderNo) ) { return null; }
//		if ( !StringUtils.isNumeric (orderNo) ) { return null; }
//		if ( orderNo.length () != 20 ) { return null; }
//		
//		long numA = Integer.parseInt (StringUtils.substring (orderNo, 0, 5));
//		long numB = Integer.parseInt (StringUtils.substring (orderNo, 5, 10));
//		long numC = Integer.parseInt (StringUtils.substring (orderNo, 10, 15));
//		long numD = Integer.parseInt (StringUtils.substring (orderNo, 15, 20));
//		
//		long numAD = numA * numD % 1000;
//		long numBC = numB * numC % 1000;
//		
//		String sumValue = String.valueOf (numAD + numBC);
//		
//		int sum = 0;
//		for ( int i = 0; i < sumValue.length (); i++ ) {
//			sum += Integer.parseInt (StringUtils.substring (sumValue, i, i + 1));
//		}
//		
//		StringBuffer sbSumValue = new StringBuffer();
//		sbSumValue.append(sumValue).append(sum % 2);
//		
//		return sbSumValue.toString();
//	}
	
	/**
	 * 숫자형 직원번호 앞자리 0 제거
	 * @param empNo
	 * @return
	 */
	public static String getConvertEmpNo ( String empNo ) {
		String returnValue = "";
		if ( StringUtils.isEmpty (empNo) ) { return returnValue; }
		if(!StringUtils.isNumeric(empNo)) { return empNo;}
		
		returnValue = String.valueOf(Long.parseLong(empNo));
		
		return returnValue;
	}
	
	/**
	 * 사이렌오더 NEW 카테고리 리스트 리턴	<br>
	 * 		ex. (new AppFunction(env)).getXoNewCateList()
	 * @return
	 */
	public List<String> getXoNewCateList(){
		List<String> cateCodeList = new ArrayList<String>();
		cateCodeList.add(this.dConf.getProperty("xo.new.cate.drink"));	//음료 NEW 카테고리
		cateCodeList.add(this.dConf.getProperty("xo.new.cate.food"));	//푸드 NEW 카테고리
		cateCodeList.add(this.dConf.getProperty("xo.new.cate.bean"));	//원두 NEW 카테고리
		
		return cateCodeList;
	}
	
	/**
	 * 사이렌오더 추천 카테고리 리스트 리턴	<br>
	 * 		ex. (new AppFunction(env)).getXoRecommendCateList()
	 * @return
	 */
	public List<String> getXoRecommendCateList(){
		List<String> cateCodeList = new ArrayList<String>();
		cateCodeList.add(this.dConf.getProperty("xo.recommend.cate.drink"));	//음료 추천 카테고리
		cateCodeList.add(this.dConf.getProperty("xo.recommend.cate.food"));		//푸드 추천 카테고리
		cateCodeList.add(this.dConf.getProperty("xo.recommend.cate.bean"));		//원두 추천 카테고리
		
		return cateCodeList;
	}
	
	/**
	 * 사이렌오더 NEW, 추천, 리저브 카테고리 리스트 리턴	<br>
	 * 		ex. (new AppFunction(env)).getXoRecommendCateList()
	 * @return
	 */
	public List<String> getXoNewRecommendCateList(){
		List<String> cateCodeList = new ArrayList<String>();
		cateCodeList.add(this.dConf.getProperty("xo.new.cate.drink"));	     // 음료 NEW 카테고리
		cateCodeList.add(this.dConf.getProperty("xo.new.cate.food"));	     // 푸드 NEW 카테고리
		cateCodeList.add(this.dConf.getProperty("xo.new.cate.bean"));	     // 원두 NEW 카테고리
		cateCodeList.add(this.dConf.getProperty("xo.recommend.cate.drink")); // 음료 추천 카테고리
		cateCodeList.add(this.dConf.getProperty("xo.recommend.cate.food"));	 // 푸드 추천 카테고리
		cateCodeList.add(this.dConf.getProperty("xo.recommend.cate.bean"));	 // 원두 추천 카테고리
		
		return cateCodeList;
	}
	
	/**
	 * 사이렌오더 메뉴별 유형 조회
	 * 
	 * @param newClassCd : 신카테고리코드
	 * @return : 메뉴타입
	 */
	public static String getMenuType(String newClassCd) {
		String menuType = "";
		
		try{
			if(newClassCd.length() == 13) {
				if(StringUtils.equals(newClassCd.substring(0, 10), "2030010020")) {
					menuType = "A";	// 아포가토
				} else if(StringUtils.equals(newClassCd.substring(0, 7), "1010020")) {   // 리저브 고도화로  리저브 newClassCd 추가 
					menuType = "R";	// 리저브
				} else if(StringUtils.equals(newClassCd.substring(0, 7), "1020020")) {   // 리저브 고도화로  리저브 newClassCd 추가
					menuType = "R";	// 리저브
				} else if(StringUtils.equals(newClassCd.substring(0, 7), "1030040")) {   // 리저브 고도화로  리저브 newClassCd 추가
					menuType = "R";	// 리저브
				} else if(StringUtils.equals(newClassCd.substring(0, 7), "1040070")) {   // 리저브 고도화로  리저브 newClassCd 추가
					menuType = "R";	// 리저브	
				} else if(StringUtils.equals(newClassCd.substring(0, 10), "3010010050")) {
					menuType = "V";	// 리저브 홀빈
				} else if(StringUtils.equals(newClassCd.substring(0, 4), "1050")) {
					menuType = "T";	// RTD
				} else if(StringUtils.equals(newClassCd.substring(0, 2), "10")) {
					menuType = "D";	// 음료
				} else if(StringUtils.equals(newClassCd.substring(0, 2), "20")) {
					menuType = "F";	// 푸드
				} else if(StringUtils.equals(newClassCd.substring(0, 2), "30")) {
					menuType = "B";	// 원두/기타
				}
			}
		}catch(Exception ex){
			menuType = "";
		}
		
		return menuType;
	}

	/**
	 * 기초 재고량을 가지고 해당 SKU의 판매 가능 여부를 체크
	 * @param cartStockList
	 * @return
	 */
//	public static Map<String, Map<String, Integer>> checkCartStock(List<CartStockInfoDto> cartStockList){
//		Map<String, Map<String, Integer>> itemMap = new HashMap<String, Map<String,Integer>>();
//		
//		/*
//		 * cartStockList의 데이터 구성
//		 * item_seq = 0  : 현 매장의 SKU별 재고량
//		 * item_seq != 0 : 재고 계산(차감)을 해야하는 SKU
//		 */
//		
//		//1. 현재 매장의 SKU별 재고량을 설정 ==========================================
//		Map<String, Integer> currentStockMap = new HashMap<String, Integer>(); 
//		Set<Integer> itemSet = new HashSet<Integer>();
//		
//		for(CartStockInfoDto dto : cartStockList){
//			if(StringUtils.equals(dto.getItemSeq(), "0")){
//				currentStockMap.put(dto.getSkuNo(), Integer.parseInt(dto.getStockCnt()));
//			}else{
//				itemSet.add(Integer.parseInt(dto.getItemSeq()));
//			}
//		}
//		
//		//2. item_seq를 정렬처리 (재고 계산시 item_seq가 낮은 메뉴부터 재고를 계산) =======
//		List<Integer> itemList = new ArrayList<Integer>(itemSet);
//		Collections.sort(itemList);
//		
//		//3. item_seq별 구성 SKU 데이터를 구성 =======================================
//		//ex. 1={5210008061=1, 5110009171=2}, 2={5210008061=1, 5110009171=2}, 3={5110007147=1}
//		for(int item : itemList){
//			Map<String, Integer> skuMap = new HashMap<String, Integer>();
//			itemMap.put(String.valueOf(item), skuMap);
//			
//			for(CartStockInfoDto dto : cartStockList){
//				if(StringUtils.equals(dto.getItemSeq(), String.valueOf(item))){
//					skuMap.put(dto.getSkuNo(), Integer.parseInt(dto.getStockCnt()));
//				}
//			}
//		}
//		
//		//4. 재고 계산 ============================================================
//		String result = "";
//		Map<String, Integer> itemSkuMap = null;
//	
//		for(int itemSeq : itemList){
//			result = "";
//			itemSkuMap = new HashMap<String, Integer>();
//			
//			try{
//				itemSkuMap = itemMap.get(String.valueOf(itemSeq));
//				
//				for(String sku : itemSkuMap.keySet()){
//					StringBuffer sb = new StringBuffer(result);
//					if((currentStockMap.get(sku) - itemSkuMap.get(sku)) >= 0){
//						sb.append('Y');
//						result = sb.toString();
//					}else{
//						sb.append('S');
//						result = sb.toString();
//					}
//				}
//				
//				//4-1. 재고 부족 SKU가 하나라도 존재하는 경우 '재고 부족'처리 (현재 매장의 SKU별 재고량을 차감하지 않음)
//				if(result.contains("S")){
//					itemSkuMap.put("RESULT", 0);
//					
//				//4-2. 모두 판매 가능한 경우 현재 매장의 SKU별 재고량을 차감 후 '판매 가능'으로 설정
//				}else{
//					for(String sku : itemSkuMap.keySet()){
//						currentStockMap.put(sku, (currentStockMap.get(sku)-itemSkuMap.get(sku)));
//					}
//					
//					itemSkuMap.put("RESULT", 1);
//				}
//			}catch(Exception ex){
//				itemSkuMap.put("RESULT", 0);
//			}
//		}
//		
//		return itemMap;
//	}
	
	/**
	 * 전자영수증 url 생성
	 * @param saleDate
	 * @param storeCd
	 * @param posNo
	 * @param seqNo
	 * @return
	 */
	public String makeDigitalReceiptKey(String saleDate, String storeCd, String posNo, String seqNo) {
		String receiptUrl = "";
		String baseUrl = this.dConf.getProperty("xo.ereceipt.base.url", "https://www.istarbucks.co.kr:7443/app/eReceipt.do?paperYn=N&eReceiptKey=");
		
		try {			
			String returnKey = AES256.receiptEncryptByHex(saleDate + storeCd + posNo + seqNo);	
			receiptUrl = baseUrl + returnKey;
		} catch (Exception e) {
			receiptUrl = "";
		}
		
		return receiptUrl;
	}

	public Environment getDConf() {
		return dConf;
	}
	
}
