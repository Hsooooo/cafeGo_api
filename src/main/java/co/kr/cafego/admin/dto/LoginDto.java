package co.kr.cafego.admin.dto;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginDto {
	/**
	 * 관리자 아이디
	 */
	private String adminId;
	/**
	 * 관리자 패스워드
	 */
	private String adminPwd;
	/**
	 * 관리 매장명
	 */
	private String storeName;
	/**
	 * 관리 매장코드
	 */
	private String storeCode;
	/**
	 * 최근 로그인 접속 시간
	 */
	private Date loginDate;
	
	/** getters & setters */
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public String getAdminPwd() {
		return adminPwd;
	}
	public void setAdminPwd(String adminPwd) {
		this.adminPwd = adminPwd;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreCode() {
		return storeCode;
	}
	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
	public Date getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	
}
