package com.goldenticket.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.goldenticket.service.MemberService;

@Controller
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/login")
	public ModelAndView loginPage() {
		ModelAndView mav = new ModelAndView("login");
		return mav;
	}

	@PostMapping("/login")
	public int loginCheck(@RequestParam("email") String email,
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
	
	
}
