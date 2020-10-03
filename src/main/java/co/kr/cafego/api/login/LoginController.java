package co.kr.cafego.api.login;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.kr.cafego.api.member.MemberService;
import co.kr.cafego.common.exception.ApiException;
import co.kr.cafego.common.util.ReturnObject;
import co.kr.cafego.core.support.ApiSupport;

/**
 * 1.0. 로그인
 * @author LEEGWIHYUN
 *
 */
@RestController
@RequestMapping("/login")
public class LoginController extends ApiSupport {
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private ReturnObject ro;
	
	/**
	 * 1.0.1 로그인
	 * @param headers
	 * @param request
	 * @param response
	 * @param mode
	 * @return
	 */
	@RequestMapping(value="/login.do", method=RequestMethod.POST)
	public Object login(@RequestHeader HttpHeaders headers, HttpServletRequest request
			, HttpServletResponse response, Model model) {
		Object obj = null;
		String remote_addr = "";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		try {
//			logWrite(LoggerCode.INFO, "Start Login Process");
			// 어드민 아이디
			String adminId  = (String)request.getParameter("adminId");
			//어드민 비밀번호
			String adminPwd = (String)request.getParameter("adminPwd");
			
			if(StringUtils.isBlank(adminId) || StringUtils.isBlank(adminPwd)){
				throw new ApiException("997", "입력파라미터가 부족합니다.");
			} 
			
			
			paramMap.put("adminId",  adminId);
			paramMap.put("adminPwd", adminPwd);
			
			obj = loginService.loginSign(paramMap);
			
			remote_addr = ro.getIp(request);
			obj = ro.getObject(request, headers, model, loginService.loginSign(paramMap));
			ro.setResult(response, "000");
		}catch(ApiException ae) {
			logger.error("[로그인] 내부처리 오류 " + ae.getResultCode() + "/" + ae.getResultMessage());
			ro.setResult(response, ae.getResultCode(), ae.getResultMessage());
		}
		return obj;
	}
}
