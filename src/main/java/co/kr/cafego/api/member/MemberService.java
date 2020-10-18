package co.kr.cafego.api.member;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.cafego.api.card.CardMapper;
import co.kr.cafego.api.member.dto.MemberInfoDto;
import co.kr.cafego.api.member.dto.MemberPointInfoDto;
import co.kr.cafego.api.member.model.MemberBasicInfoModel;
import co.kr.cafego.api.payment.PaymentMapper;
import co.kr.cafego.common.exception.ApiException;
import co.kr.cafego.common.util.AppFunction;
import co.kr.cafego.common.util.DataCode;
import co.kr.cafego.common.util.DateTime;
import co.kr.cafego.common.util.ResultCode;
import co.kr.cafego.common.util.SHA256;
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
	
	@Autowired
	private PaymentMapper paymentMapper;
	
	@Autowired
	private CardMapper cardMapper;
	
	/**
	 * 1.1. 회원 가입(일반 회원가입)
	 * @param paramMap
	 * @return
	 */
	@Transactional(value="transactionManager", rollbackFor= {Exception.class, ApiException.class, SQLException.class})
	public Object emailJoin(Map<String, String> paramMap) throws Exception{
		Map<String, Object> dbMap = new HashMap<String, Object>();
		
		String memberName  = paramMap.get("memberName");
		String memberSex   = paramMap.get("memberSex");
		String memberPhone = paramMap.get("memberPhone");
		String memberEmail = paramMap.get("memberEmail");
		String joinFlag    = paramMap.get("joinFlag");
		String encPwd	   = paramMap.get("memberPwd");
		String testPwd	   = paramMap.get("testPwd");
		
		try {
			String today = DateTime.getCurrentDate(1);		//yyyyMMddHHmm
			
			String decryptPwd = "";
//			decryptPwd = AES256.decrypt(encPwd);
			decryptPwd = testPwd;
			
			String dbPwd = SHA256.encrypt(decryptPwd);
			
			dbMap.put("memberName",  memberName);
			dbMap.put("memberSex",   memberSex);
			dbMap.put("memberPhone", memberPhone);
			dbMap.put("memberEmail", memberEmail);
			dbMap.put("joinFlag",    joinFlag);
			dbMap.put("dbPwd", 		 dbPwd);
			dbMap.put("status", 	 DataCode.MEMBER_STATUS_NORMAL);
			
			//등록된 회원이 있는지 조회 (가입 경로도 같이 조회해야함
			// -> 일반 회원가입과 카카오 로그인의 경우 이메일이 같을..수있음)
			MemberInfoDto memberDto = memberMapper.getMemberInfoByEmail(dbMap);
			
			if(memberDto != null) {
				throw new ApiException(ResultCode.MEM_02, "이미 등록된 회원 정보가 있습니다.");
			}
			
			int a = memberMapper.regMember(dbMap);
			
			if(a > 0){
				//회원 가입된 회원번호 조회
				memberDto = memberMapper.getMemberInfoByEmail(dbMap);
				
				dbMap.put("memberNum", memberDto.getMemberNum());
				dbMap.put("pointAmt",  500); //가입 기념 500P
				dbMap.put("useType",   DataCode.MEMBER_POINT_TYPE_JOIN);  
				dbMap.put("today", 	   today);
				
				memberMapper.regMemberPointInfo(dbMap);
				
				paymentMapper.insertMemberPointHist(dbMap);
			}
		}catch(ApiException ae) {
			throw ae;
		}catch(SQLException se) {
			throw se;
		}catch(Exception e) {
			throw e;
		}
		return null;
	}
	
	/**
	 * Email로 로그인(일반 로그인)
	 * @param paramMap
	 * @param request
	 * @return
	 */
	public MemberBasicInfoModel emailLogin(Map<String, String> paramMap, HttpServletRequest request) throws Exception {
		MemberBasicInfoModel model = new MemberBasicInfoModel();
		Map<String, Object> dbMap = new HashMap<String, Object>();
		
		String memberEmail = paramMap.get("memberEmail");
		String memberPwd   = paramMap.get("memberPwd");
		String joinFlag    = paramMap.get("joinFlag");
		try {
			
			//Email로 회원 정보 조회
			dbMap.put("memberEmail", memberEmail);
			dbMap.put("joinFlag",    joinFlag);
			MemberInfoDto memberInfoDto = memberMapper.getMemberInfoByEmail(dbMap);
			
			//전달받은 pwd SHA256 암호화(AES256 복호화 필요 ** 테스트할 경우 일반 Text로 받음)
			String dbPwd = SHA256.encrypt(memberPwd);
			
			//정상 로그인 성공시
			if(StringUtils.equals(dbPwd, memberInfoDto.getMemberPwd()) && memberInfoDto.getFailCnt() <= 10){
				//로그인 성공시 Session에 회원 정보 저장
				request.getSession().setAttribute(DataCode.MEMBER_SESSION_NAME, memberInfoDto);
			}
			//로그인 실패 횟수가 10회 이상일 경우
			else if(memberInfoDto.getFailCnt() > 10) {

				throw new ApiException(ResultCode.MEM_04, "로그인 실패 횟수 초과(패스워드 재설정 필요)");
			}
			//전달받은 pwd를 암호화한 값이 DB에 저장된 pwd값과 다를 경우
			else if(!StringUtils.equals(dbPwd, memberInfoDto.getMemberPwd())) {
				int a = memberMapper.updateFailCnt(dbMap);
				throw new ApiException(ResultCode.MEM_03, "패스워드가 일치하지 않습니다.");
			}
			model.setFailCnt(Integer.toString(memberInfoDto.getFailCnt()));
			model.setMemberName(memberInfoDto.getMemberName());
			model.setMemberNum(memberInfoDto.getMemberNum());
			model.setMemberEmail(memberInfoDto.getMemberEmail());
			model.setJoinFlag(memberInfoDto.getJoinFlag());
			model.setMemberSex(memberInfoDto.getMemberSex());
			model.setMemberPhone(memberInfoDto.getMemberPhone());
			
		}catch(ApiException ae) {
			throw ae;
		}
		return model;
	}

	
	/**
	 * 1.1. 회원 가입(일반 회원가입)
	 * @param paramMap
	 * @return
	 * @throws SQLException 
	 */
	@Transactional(value="transactionManager", rollbackFor= {Exception.class, ApiException.class})
	public Object memberJoin(Map<String, String> paramMap) throws SQLException {
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
		}catch(SQLException se) {
			logger.error(se.getMessage());
			throw se;		// throw 하게되면 상단(Controller)에서도 Exception 처리를 하겠다 라는 의미
		}catch(Exception e) {
			throw e;
		}
		return model;
	}
	
	@Transactional(value="transactionManager", rollbackFor= {Exception.class, ApiException.class, SQLException.class})
	public Object memberCardReg(Map<String, String> paramMap) throws SQLException {
		Map<String, Object> dbMap = new HashMap<String, Object>();
		try {
			String memberNum = paramMap.get("memberNum");
			
			boolean cardDup = true;
			String cardNum = "";
			
			while(cardDup) {
				
				String nowDay = DateTime.getCurrentDate(14);
				String cardSeq = cardMapper.getCardSeq();
				
				cardNum = nowDay + memberNum + cardSeq;
				dbMap.put("cardNum", cardNum);
				dbMap.put("memberNum", memberNum);
				int dupCnt = memberMapper.memberCardDupCheck(dbMap);
				
				if(dupCnt == 0) {
					cardDup = false;
					cardNum = nowDay + memberNum + cardSeq;
					dbMap.put("cardNum",   cardNum);
					dbMap.put("amount",    0);
					dbMap.put("status",    "00");
					
					int regCnt = memberMapper.regMemberCard(dbMap);
				}
			}
		}catch(SQLException se) {
			logger.error(se.getMessage());
			throw se;		// throw 하게되면 상단(Controller)에서도 Exception 처리를 하겠다 라는 의미
		}catch(ApiException ae) {
			
		}
		
		return null;
	}
	
	
}
