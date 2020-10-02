package co.kr.cafego.api.member;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import co.kr.cafego.core.support.ApiSupport;


/**
 * 1. 회원
 * @author Hsooooo
 *
 */
@RestController
@RequestMapping("/member")
public class MemberController extends ApiSupport {
	
	@Autowired
	private MemberService memberService;
	
	
	/**
	 * 1.1. 회원 가입 (일반 회원가입)
	 * @param headers
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/memberJoin.do", method=RequestMethod.POST)
	public Object memberJoin(@RequestHeader HttpHeaders headers, HttpServletRequest request
			, HttpServletResponse response, Model model) {
		
		Object obj = null;
		Map<String, String> paramMap = new HashMap<String, String>();
		
		
		obj = memberService.memberJoin(paramMap);
		
		return obj;
		
	}

	/**
	 * 1.2. 로그인 (ADMIN 로그인)
	 * @param headers
	 * @param request
	 * @return
	 */
	
	public Object login(@RequestHeader HttpHeaders headers, HttpServletRequest request
			, HttpServletResponse response, Model mode) {
		
		Object obj = null;
		Map<String, String> paramMap = new HashMap<String, String>();
		
		String adminId = "";
		String adminPw = "";
		// 어드민 아이디
		adminId = ((String)request.getParameter("adminId"));
		//어드민 비밀번호
		adminPw = ((String)request.getParameter("adminPw"));
		
		paramMap.put("adminId", adminId);
		paramMap.put("adminPw", adminPw);
		
		
		obj = memberService.loginSign(paramMap);
		
		return obj;
	}
}
