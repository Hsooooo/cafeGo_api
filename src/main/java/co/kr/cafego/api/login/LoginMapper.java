package co.kr.cafego.api.login;

import java.util.Map;

import co.kr.cafego.api.login.dto.LoginDto;

public interface LoginMapper {

	/**
	 * 로그인 회원 정보
	 * @param dbMap
	 * @return
	 */
	public LoginDto getLoginMemberInfo(Map<String, Object> dbMap);
	
	/**
	 * 관리자 로그인 정보
	 * @param dbMap
	 * @return
	 */
	public String loginInfo(Map<String, Object> dbMap);
	
	/**
	 * 로그인 성공 접속 시간 업데이트
	 * @param dbMap
	 * @return
	 */
	public String loginScsUpdate(Map<String, Object> dbMap);
	
}
