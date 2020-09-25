package co.kr.cafego.api.member;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.kr.cafego.core.support.ApiSupport;

/**
 * 1. 회원
 * @author Hsooooo
 *
 */
@Service
public class MemberService extends ApiSupport{

	@Autowired
	private MemberMapper memberMapper;
	
	/**
	 * 1.1. 회원 가입(일반 회원가입)
	 * @param paramMap
	 * @return
	 */
	public Object memberJoin(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
