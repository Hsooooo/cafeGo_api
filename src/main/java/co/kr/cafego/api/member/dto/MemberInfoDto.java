package co.kr.cafego.api.member.dto;

public class MemberInfoDto {
	/** 회원 번호*/
	private String memberNum;
	/** 회원 명*/
	private String memberName;
	/** 회원 성별*/
	private String memberSex;
	/** 회원 휴대폰*/
	private String memberPhone;
	/** 회원 이메일*/
	private String memberEmail;
	/** 가입 구분*/
	private String joinFlag;
	/** 회원 상태*/
	private String status;
	/** 회원 PWD(SHA256 암호화 된 값)*/
	private String memberPwd;
	
	/** 로그인 실패 횟수*/
	private int failCnt;
	
	
	/**
	 * 회원 번호
	 * @return
	 */
	public String getMemberNum() {
		return memberNum;
	}
	/**
	 * 회원 번호
	 * @param memberNum
	 */
	public void setMemberNum(String memberNum) {
		this.memberNum = memberNum;
	}
	/**
	 * 회원 명
	 * @return
	 */
	public String getMemberName() {
		return memberName;
	}
	/**
	 * 회원 명
	 * @param memberName
	 */
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	/**
	 * 회원 성별
	 *  M : 남성
	 *  F : 여성
	 * @return
	 */
	public String getMemberSex() {
		return memberSex;
	}
	/**
	 * 회원 성별
	 *  M : 남성
	 *  F : 여성
	 * @param memberSex
	 */
	public void setMemberSex(String memberSex) {
		this.memberSex = memberSex;
	}
	/**
	 * 회원 휴대폰
	 * @return
	 */
	public String getMemberPhone() {
		return memberPhone;
	}
	/**
	 * 회원 휴대폰
	 * @param memberPhone
	 */
	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}
	/**
	 * 회원 이메일
	 * @return
	 */
	public String getMemberEmail() {
		return memberEmail;
	}
	/**
	 * 회원 이메일
	 * @param memberEmail
	 */
	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}
		/**
	 * 가입 구분
	 *  K : 카카오
	 *  N : 네이버
	 *  F : 페이스북
	 *  E : 일반회원가입
	 * @return
	 */
	public String getJoinFlag() {
		return joinFlag;
	}
	/**
	 * 가입 구분
	 *  K : 카카오
	 *  N : 네이버
	 *  F : 페이스북
	 *  E : 일반회원가입
	 * @param joinFlag
	 */
	public void setJoinFlag(String joinFlag) {
		this.joinFlag = joinFlag;
	}
	/**
	 * 회원 상태
	 *  W : 탈퇴 정보 존재(복구 가능)
	 *  U : 정상 상태
	 *  B : 블랙리스트 등록된 상태
	 * @return
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 회원 상태
	 *  W : 탈퇴 정보 존재(복구 가능)
	 *  U : 정상 상태
	 *  B : 블랙리스트 등록된 상태
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 회원 PWD(SHA256 암호화 된 값)
	 * @return
	 */
	public String getMemberPwd() {
		return memberPwd;
	}
	/**
	 * 회원 PWD(SHA256 암호화 된 값)
	 * @param memberPwd
	 */
	public void setMemberPwd(String memberPwd) {
		this.memberPwd = memberPwd;
	}
	/**
	 * 로그인 실패 횟수
	 * @return
	 */
	public int getFailCnt() {
		return failCnt;
	}
	/**
	 * 로그인 실패 횟수
	 * @param failCnt
	 */
	public void setFailCnt(int failCnt) {
		this.failCnt = failCnt;
	}
	
	
	
}
