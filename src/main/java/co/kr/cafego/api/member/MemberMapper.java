package co.kr.cafego.api.member;

import java.util.Map;

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
}
