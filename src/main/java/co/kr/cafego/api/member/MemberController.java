package co.kr.cafego.api.member;

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

import co.kr.cafego.api.member.model.MemberBasicInfoModel;
import co.kr.cafego.common.exception.ApiException;
import co.kr.cafego.common.util.ResultCode;
import co.kr.cafego.common.util.ReturnObject;
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
	
	@Autowired
	private ReturnObject ro;
	
	
	/**
	 * 1.1. 회원 가입 (일반 회원가입)
	 * @param headers
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/emailJoin.do", method=RequestMethod.POST)
	public Object emailJoin(@RequestHeader HttpHeaders headers, HttpServletRequest request
			, HttpServletResponse response, Model model) {
		
		Object obj = null;
		Map<String, String> paramMap = new HashMap<String, String>();
		
		@SuppressWarnings("unchecked")
		Map<String, Object> parameters = (Map<String, Object>) request.getAttribute("bodyMap");
		
		try {
			String memberName  = (String) parameters.get("memberName");
			String memberSex   = (String) parameters.get("memberSex");
			String memberPhone = (String) parameters.get("memberPhone");
			String memberEmail = (String) parameters.get("memberEmail");
			String joinFlag    = (String) parameters.get("joinFlag");
			// FRONT-SIDE에서 AES256 암호화 방식 가능한지
			// API단 테스트일 경우 일반 PlainText로 테스트
			String encPwd	   = (String) parameters.get("memberPwd");
			String testPwd	   = (String) parameters.get("testPwd");
			
			
			//---------------상단 파라미터 전부 필수 --------------------//
			paramMap.put("memberName",  memberName);
			paramMap.put("memberSex",   memberSex);
			paramMap.put("memberPhone", memberPhone);
			paramMap.put("memberEmail", memberEmail);
			paramMap.put("encPwd", 		encPwd);
			paramMap.put("joinFlag", 	joinFlag);
			paramMap.put("testPwd", 	testPwd);
			
			obj = memberService.emailJoin(paramMap);
			ro.setResult(response, ResultCode.SUCCESS);
		}catch(ApiException ae) {
			logger.error("[{}][{}] ApiException", "회원 가입(일반 회원가입)", ae.getResultMessage());
			ro.setResult(response, ae.getResultCode());
		}catch(Exception e) {
			logger.error("Exception ->", e);
			ro.setResult(response, ResultCode.SERVER_ERROR);
		}
		
		return obj;
		
	}
	
	/**
	 * 1.2. 로그인
	 * @param headers
	 * @param request
	 * @param response
	/ * @param model
	 * @return
	 */
	@RequestMapping(value="/login.do", method=RequestMethod.POST)
	public Object paymentList(@RequestHeader HttpHeaders headers, HttpServletRequest request
			, HttpServletResponse response, Model model) {
		MemberBasicInfoModel memberModel = new MemberBasicInfoModel();
		Object obj = null;
		Map<String, String> paramMap = new HashMap<String, String>();
		
		@SuppressWarnings("unchecked")
		Map<String, Object> parameters = (Map<String, Object>) request.getAttribute("bodyMap");
		
		try {
			String memberEmail = (String) parameters.get("memberEmail");
			String memberPwd   = (String) parameters.get("memberPwd");
			String joinFlag	   = (String) parameters.get("joinFlag");
			
			if(StringUtils.isBlank(memberPwd) || StringUtils.isBlank(memberEmail) || StringUtils.isBlank(joinFlag)) {
				throw new ApiException(ResultCode.INVALID_PARAMETER, "필수 파라미터 NULL");
			}
			
			paramMap.put("memberEmail", memberEmail);
			paramMap.put("memberPwd",   memberPwd);
			paramMap.put("joinFlag", joinFlag);
			
			memberModel = memberService.emailLogin(paramMap, request);
			obj = ro.getObject(request, headers, model, memberModel);
			ro.setResult(response, ResultCode.SUCCESS);
		}catch(ApiException ae) {
			logger.error("[{}][{}] ApiException", " 로그인", ae.getResultMessage());
			ro.setResult(response, ae.getResultCode());
		}catch(Exception e) {
			logger.error("Exception ->", e);
			ro.setResult(response, ResultCode.SERVER_ERROR);
		}
		
		return obj;
		
	}
	
	@RequestMapping(value="/memberCardReg.do", method=RequestMethod.POST)
	public Object memberCardReg(@RequestHeader HttpHeaders headers, HttpServletRequest request
			, HttpServletResponse response, Model model) {
		Object obj = null;
		Map<String, String> paramMap = new HashMap<String, String>();
		
		@SuppressWarnings("unchecked")
		Map<String, Object> parameters = (Map<String, Object>) request.getAttribute("bodyMap");
		
		try {
			String memberNum  = (String) parameters.get("memberNum");
			
			paramMap.put("memberNum", memberNum);
			obj = memberService.memberCardReg(paramMap);
		
		}catch(ApiException ae) {
			
		}catch(Exception e) {
			logger.error("EXception", e);
		}
		return obj;
		
	}
}
