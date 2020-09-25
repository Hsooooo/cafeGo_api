package co.kr.cafego.api.member.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MemberBasicInfoModel {
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
	public String getMemberNum() {
		return memberNum;
	}
	public void setMemberNum(String memberNum) {
		this.memberNum = memberNum;
	}
	public String getMemberEmail() {
		return memberEmail;
	}
	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}
	public String getMemberSex() {
		return memberSex;
	}
	public void setMemberSex(String memberSex) {
		this.memberSex = memberSex;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getJoinFlag() {
		return joinFlag;
	}
	public void setJoinFlag(String joinFlag) {
		this.joinFlag = joinFlag;
	}
	public String getMemberStatus() {
		return memberStatus;
	}
	public void setMemberStatus(String memberStatus) {
		this.memberStatus = memberStatus;
	}
	
	
}
