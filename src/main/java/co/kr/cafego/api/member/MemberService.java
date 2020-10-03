package co.kr.cafego.api.member;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.cafego.api.member.dto.MemberPointInfoDto;
import co.kr.cafego.api.member.model.MemberBasicInfoModel;
import co.kr.cafego.common.exception.ApiException;
import co.kr.cafego.common.util.AppFunction;
import co.kr.cafego.common.util.ResultCode;
import co.kr.cafego.core.support.ApiSupport;

/**
 * 1. 회원
 * @author Hsooooo
 *
 */
@Service
public class MemberService extends ApiSupport{

	@Autowired
	private MemberMapper memberMapper;
	
	/**
	 * 1.1. 회원 가입(일반 회원가입)
	 * @param paramMap
	 * @return
	 */
	@Transactional(value="transactionManager", rollbackFor= {Exception.class, ApiException.class})
	public Object memberJoin(Map<String, String> paramMap) {
		MemberBasicInfoModel model = new MemberBasicInfoModel();
		//Controller에서 전달된 값 변수 저장
		String memberName = paramMap.get("memberName");
		String memberSex  = paramMap.get("memberSex");
		String phone	  = paramMap.get("memberPhone");
		String email	  = paramMap.get("memberEmail");
		
		try {
			// step 1. 변수 유효성 검사 (변수가 유효한 변수인지)
			// -> 이런식으로 스텝 별로 묶어서 주석 표시해주면 나중에 보기 편해용
			
			// 유효성 검사 예시
			// -> 지금은 무조건 false로 리턴하기때문에 ApiException 발생하지 않음
			// -> but ApiException 발생시 현재 메서드 최상단에 rollbackFor로 잡힌 클래스가
			//    ApiException도 같이 있으므로 발생하게 되면 롤백 수행
//			if(validateSample()) {
//				throw new ApiException(ResultCode.EXT_01);
//			}
			
			// setp 2. DB 저장
			// -> 지금은 회원 가입시 들어갈 정보가 회원 정보 테이블 하나이기 때문에 복잡하지 않음
			// -> but 포인트 정보테이블에 데이터 한번에 넣어야함
			// -> 포인트 이력 테이블(첫 가입 포인트) 및 포인트 테이블 (0원으로 초기화 데이터)
			Map<String, Object> dbMap = new HashMap<String, Object>();
			dbMap.put("memberName", memberName);
			dbMap.put("memberSex",  memberSex);
			dbMap.put("phone", 		phone);
			dbMap.put("email", 		email);
			
			int joinCnt = memberMapper.memberJoin(dbMap);
			
			if(joinCnt < 1) {
				//상단 주석 표기를 보면 여기서도 롤백이 수행될 것을 알 수 있음
				throw new ApiException(ResultCode.INVALID_REQUEST);
			}
			
			// DB처리를 한번 마치고 다른 쿼리를 수행해야 할 경우 clear 후 다시 세팅
			dbMap.clear();
			dbMap.put("email", email);
			
			// 지금은 포인트 정보라고 했지만 포인트 정보를 갖고온건 쿼리가 조금 복잡할 경우
			// 쿼리문 인덴트 맞추는 예시..
			// 항상 쿼리 조회 결과는 Dto로 끝나는 클래스
			MemberPointInfoDto pointDto = memberMapper.getMemberPointInfo(dbMap);
			// TODO 지금 로직상 가입 후 회원의 기본 정보를 불러와야 맞으므로
			//      쿼리는 기본 정보를 불러오는 로직을 짜야함
			//      근데 트랜잭션 커밋이 안됐는데 방금 인서트한 데이터를 조회할수 있는지 모르겠네요
			
			
			// 전문의 리턴 형식은 항상 Model로 끝나는 클래스
			// -> 클래스 이름은 항상 그 목적이 분명하게 드러나게, 간결하게, 단어별 첫글자는 대문자로!
			model.setJoinFlag(pointDto.getJoinFlag());
			model.setMemberEmail(pointDto.getMemberEmail());
			model.setMemberName(pointDto.getMemberName());
			model.setMemberNum(pointDto.getMemberNum());
			model.setMemberSex(pointDto.getMemberSex());
			
			//TODO 자바 변수명은 모두 카멜케이스로 작성
			//     DB 컬럼명은 DB는 대소문자를 구분하지 않기때문에 _케이스 사용
			
		}catch(ApiException ae) {
			logger.error(ae.getMessage());
			throw ae;		// throw 하게되면 상단(Controller)에서도 Exception 처리를 하겠다 라는 의미
		}catch(Exception e) {
			throw e;
		}
		return model;
	}
	
	@Transactional(value="transactionManager", rollbackFor= {Exception.class, ApiException.class, SQLException.class})
	public Object memberCardReg(Map<String, String> paramMap) {
		Map<String, Object> dbMap = new HashMap<String, Object>();
		try {
			String memberNum = paramMap.get("memberNum");
			
			boolean cardDup = true;
			String cardNum = "";
			
			while(cardDup) {
				String tmpCardNum = AppFunction.getMemberCardNum();
				dbMap.put("cardNum", tmpCardNum);
				int dupCnt = memberMapper.memberCardDupCheck(dbMap);
				
				if(dupCnt == 0) {
					cardDup = false;
					cardNum = tmpCardNum;
					dbMap.put("memberNum", memberNum);
					dbMap.put("cardNum",   cardNum);
					dbMap.put("amount",    0);
					dbMap.put("status",    "00");
					
					int regCnt = memberMapper.regMemberCard(dbMap);
				}
			}
		}catch(ApiException ae) {
			
		}
		
		return null;
	}
	
}
