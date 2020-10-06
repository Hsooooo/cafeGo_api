package co.kr.cafego.api.member;

import java.util.Map;

import co.kr.cafego.api.member.dto.MemberInfoDto;
import co.kr.cafego.api.member.dto.MemberPointInfoDto;

public interface MemberMapper {
	
	/**
	 * 회원 보유 포인트 조회(예시)
	 * @param dbMap
	 * @return
	 */
	public MemberPointInfoDto getMemberPointInfo(Map<String, Object> dbMap);
	
	/**
	 * 회원 가입
	 * @param dbMap
	 * @return
	 */
	public int memberJoin(Map<String, Object> dbMap);
	
	/**
	 * 중복 카드 체크
	 * @param dbMap
	 * @return
	 */
	public int memberCardDupCheck(Map<String, Object> dbMap);

	/**
	 * 회원 카드 등록
	 * @param dbMap
	 * @return
	 */
	public int regMemberCard(Map<String, Object> dbMap);

	/**
	 * 회원 가입
	 * @param dbMap
	 * @return
	 */
	public int regMember(Map<String, Object> dbMap);
	
	/**
	 * 회원 포인트 정보 등록
	 * @param dbMap
	 * @return
	 */
	public int regMemberPointInfo(Map<String, Object> dbMap);
	
	/**
	 * 회원 Email로 정보 조회
	 * @param dbMap
	 * @return
	 */
	public MemberInfoDto getMemberInfoByEmail(Map<String, Object> dbMap);

	/**
	 * 로그인 실패 카운트 업데이트
	 * @param dbMap
	 * @return
	 */
	public int updateFailCnt(Map<String, Object> dbMap);
}
