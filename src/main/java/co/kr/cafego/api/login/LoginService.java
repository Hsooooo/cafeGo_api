package co.kr.cafego.api.login;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import co.kr.cafego.common.util.DataCode;
import co.kr.cafego.api.login.model.LoginModel;
import co.kr.cafego.api.member.MemberMapper;
import co.kr.cafego.common.exception.ApiException;
import co.kr.cafego.common.util.ResultCode;
import co.kr.cafego.core.support.ApiSupport;

public class LoginService  extends ApiSupport{
	
	@Autowired
	private LoginMapper loginMapper;
	
	/**
	 * 1.0.1. 로그인
	 * @param paramMap
	 * @return
	 */
	@Transactional(value="transactionManager", rollbackFor= {Exception.class, ApiException.class})
	public Object loginSign(Map<String, String> paramMap) {
		LoginModel model = new LoginModel();
		String adminId  = paramMap.get("adminId");
		String adminPwd = paramMap.get("adminPwd");
		try {
			Map<String, Object> dbMap = new HashMap<String, Object>();
			dbMap.put("adminId",  adminId );
			dbMap.put("adminPwd", adminPwd);
			
			String loginSuccessYn = "";
			String loginInfo = loginMapper.loginInfo(dbMap);
			
			// 로그인 성공 플래그값(Y: 회원정보 있음      N:회원 정보 없음)
			// (LoginSuccess != NULL) ? Y : N
			// 플래그 값 가지고 와서 NULL이 아니면 접속 성공으로 생각하고 접속 시간 업데이트 처리 필요
			if(StringUtils.isNotEmpty(loginInfo)) {
				loginSuccessYn = DataCode.LOGIN_SUCESS;
			} else {
				loginSuccessYn = DataCode.LOGIN_FAIL;
				throw new ApiException(ResultCode.DB_ERROR);
			}
			
			if( "Y".equals(loginSuccessYn) ) {
				String loginScsUpdate = loginMapper.loginScsUpdate(dbMap);
			} else {
				throw new ApiException(ResultCode.DB_ERROR);
			}
			
		}catch(ApiException ae) {
			logger.error(ae.getMessage());
			throw ae;		// throw 하게되면 상단(Controller)에서도 Exception 처리를 하겠다 라는 의미
		}catch(Exception e) {
			throw e;
		}
		return model;
	}

}
