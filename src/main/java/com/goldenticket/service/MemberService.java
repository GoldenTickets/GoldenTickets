package com.goldenticket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goldenticket.DTO.Member;
import com.goldenticket.mapper.MemberMapper;

@Service
public class MemberService {
	
	@Autowired
	private MemberMapper memberMapper;
	
	//로그인 기능
	public int loginCheck(String email,String password) {//보안적으로 문제가없는가?
		//DB에서 비밀번호가일치하는지 확인하기 vs DB에서 받아온값을 Service에서 비교해서 비밀번호가일치하는지확인
		String pwfromdb=memberMapper.logincheck(email);
		if(password.equals(pwfromdb)) {//DB에서 받아온 비밀번호가 사용자가 전송한 비밀번호와 일치한다면
			return 1;
		}else {//DB에서 받아온 비밀번호가 사용자가 전송한 비밀번호와 일치하지않다면
			return 0;
		}
	}
	
	
	//닉네임,회원번호 가져오기
	public Member getMember(String email) {
		return memberMapper.getMember(email);
	}
	
	//회원가입하기
	public int createMember(Member member) {
		return memberMapper.createMember(member);
	}
	
}