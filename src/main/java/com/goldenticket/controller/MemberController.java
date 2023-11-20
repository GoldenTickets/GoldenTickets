package com.goldenticket.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.goldenticket.DTO.Member;
import com.goldenticket.service.MemberService;

@RestController
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/login")
	public ModelAndView loginPage(HttpSession session){
		
		if(session.getAttribute("id") == null) {
			return new ModelAndView("/login");
		}else {
			return new ModelAndView("redirect:/");
		}
		
	}
	
	@PostMapping("/login")
	public ModelAndView login(@RequestParam("email") String email,
			 					  @RequestParam("password") String password,
			 					  HttpSession session) {
		int result = memberService.loginCheck(email,password);
		
		int membeno=memberService.getMember(email).getId();
		String nickname=memberService.getMember(email).getNickname();
		
		if(result==1) {
			session.setAttribute("id",membeno);
			session.setAttribute("nickname",nickname);
			
			return new ModelAndView ("redirect:/");
		}else {
			return new ModelAndView ("redirect:/");
		}
		
	}

	
	@PostMapping("/loginwithcheck")
	public int loginwithCheck(@RequestParam("email") String email,
							 @RequestParam("password") String password,
							 HttpSession session) {//로그인 
		int result = memberService.loginCheck(email,password);
		
		int membeno=memberService.getMember(email).getId();
		String nickname=memberService.getMember(email).getNickname();
				
		
		if(result==1) {
			session.setAttribute("id",membeno);
			session.setAttribute("nickname",nickname);
			return 1;
		}else {
			return 0;
		}
	}
	
	@GetMapping("/logout")
	public ModelAndView logout(HttpSession session) {
		session.invalidate();
		return new ModelAndView("main"); 
	}
	
	@GetMapping("/signup")
	public ModelAndView signup(HttpSession session) {
		if(session.getAttribute("nickname")!=null) {
			return new ModelAndView("main");
		}else {
			return new ModelAndView("signup"); 
		}
	}
	
	@PostMapping("/signup")
	public ModelAndView createMember(@RequestBody Member member) {
		memberService.createMember(member);
		return new ModelAndView("redirect:/login");
	}
}
