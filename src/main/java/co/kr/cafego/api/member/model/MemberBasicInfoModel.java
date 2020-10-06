package co.kr.cafego.api.member.model;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 회원 기본 정보
 * @author Hsooooo
 *
 */
public class MemberBasicInfoModel {
	//TODO 주석써야하는데 귀찮아서..일단은
	
	
	@JsonProperty("memberNum")
	private String memberNum;
	@JsonProperty("memberEmail")
	private String memberEmail;
	@JsonProperty("memberSex")
	private String memberSex;
	@JsonProperty("memberName")
	private String memberName;
	@JsonProperty("joinFlag")
	private String joinFlag;
	@JsonProperty("memberStatus")
	private String memberStatus;
	@JsonProperty("memberPhone")
	private String memberPhone;
	@JsonProperty("failCnt")
	private String failCnt;
	public String getMemberNum() {
		return StringUtils.defaultIfBlank(memberNum,"");
	}
	public void setMemberNum(String memberNum) {
		this.memberNum = memberNum;
	}
	public String getMemberEmail() {
		return StringUtils.defaultIfBlank(memberEmail,"");
	}
	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}
	public String getMemberSex() {
		return StringUtils.defaultIfBlank(memberSex, "");
	}
	public void setMemberSex(String memberSex) {
		this.memberSex = memberSex;
	}
	public String getMemberName() {
		return StringUtils.defaultIfBlank(memberName, "");
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getJoinFlag() {
		return StringUtils.defaultIfBlank(joinFlag, "");
	}
	public void setJoinFlag(String joinFlag) {
		this.joinFlag = joinFlag;
	}
	public String getMemberStatus() {
		return StringUtils.defaultIfBlank(memberStatus, "");
	}
	public void setMemberStatus(String memberStatus) {
		this.memberStatus = memberStatus;
	}
	public String getMemberPhone() {
		return StringUtils.defaultIfBlank(memberPhone, "");
	}
	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}
	public String getFailCnt() {
		return StringUtils.defaultIfBlank(failCnt, "0");
	}
	public void setFailCnt(String failCnt) {
		this.failCnt = failCnt;
	}
	
	
	
	
}
