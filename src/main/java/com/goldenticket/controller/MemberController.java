package com.goldenticket.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.goldenticket.DTO.Member;
import com.goldenticket.mapper.MemberMapper;

@RestController
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private MemberMapper memberMapper;
	
	@PostMapping("")
	public Member signup(@RequestBody Member member) { //회원가입 -> fetch로 할것 // 아직 미완
		memberMapper.signup(member);
		return member;
	}
	
	@GetMapping("")
	public Member login(@RequestParam Member member, HttpSession session) { //로그인도 -> fetch로 할것 로그인 정보 확인 로직은 나중에 처리하기
		member = memberMapper.login(member);
		session.setAttribute("mem_id", member.getId());
		session.setAttribute("nickname", member.getNickname());
		return member;
	}
}
