package co.kr.cafego.api.login.model;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author chung
 *
 */
public class LoginModel {
	/**
	 * 관리자 아이디
	 */
	@JsonProperty("adminId")
	private String adminId;
	/**
	 * 관리자 패스워드
	 */
	@JsonProperty("adminPwd")
	private String adminPwd;
	/**
	 * 관리 매장명
	 */
	@JsonProperty("storeName")
	private String storeName;
	/**
	 * 관리 매장코드
	 */
	@JsonProperty("storeCode")
	private String storeCode;
	/**
	 * 최근 로그인 접속 시간
	 */
	@JsonProperty("loginDate")
	private Date loginDate;
	
	
	/**   GETTER & SETTER   */
	/**
	 * 관리자 아이디
	 */
	public String getAdminId() {
		return adminId;
	}
	/**
	 * 관리자 아이디
	 */
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	
	/**
	 * 관리자 패스워드
	 * getAdminPwd
	 */
	public String getAdminPwd() {
		return adminPwd;
	}
	/**
	 * 관리자 패스워드
	 * setAdminPwd
	 */
	public void setAdminPwd(String adminPwd) {
		this.adminPwd = adminPwd;
	}
	
	/**
	 * 관리 매장명
	 * getStoreName
	 */
	public String getStoreName() {
		return storeName;
	}
	/**
	 * 관리 매장명
	 * setStoreName
	 */
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	
	/**
	 * 관리 매장코드
	 * getStoreCode
	 */
	public String getStoreCode() {
		return storeCode;
	}
	/**
	 * 관리 매장코드
	 * setStoreCode
	 */
	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
	
	/**
	 * 최근 로그인 접속 시간
	 * getLoginDate
	 */
	public Date getLoginDate() {
		return loginDate;
	}
	/**
	 * 최근 로그인 접속 시간
	 * setLoginDate
	 */
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	
	
	/**     TOSTRING     */
	@Override
	public String toString() {
		return "LoginModel [adminId=" + adminId + ", adminPwd=" + adminPwd + ", storeName=" + storeName + ", storeCode="
				+ storeCode + ", loginDate=" + loginDate + "]";
	}
	
	
	
	
}
