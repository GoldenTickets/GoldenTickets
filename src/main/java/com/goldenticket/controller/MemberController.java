package com.goldenticket.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	
	/*
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
	*/
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestParam("email") String email,
			 					  @RequestParam("password") String password,
			 					  HttpSession session) {
		int result = memberService.loginCheck(email,password);
		
		int membeno=memberService.getMember(email).getId();
		String nickname=memberService.getMember(email).getNickname();
		
		if(result==1) {
			session.setAttribute("id",membeno);
			session.setAttribute("nickname",nickname);		
			
			return new ResponseEntity<>("ok",HttpStatus.OK);
		}else {
			return new ResponseEntity<>("ok",HttpStatus.NOT_FOUND);
		}
		
	}

	
	@GetMapping("/logout")
	public ResponseEntity<String> logout(HttpSession session) {
		session.invalidate();
		return new ResponseEntity<>("logout",HttpStatus.OK); 
	}
	
	@GetMapping("/signup")//회원가입페이지로 이동
	public ModelAndView signup(HttpSession session) {
		if(session.getAttribute("nickname")!=null) {
			return new ModelAndView("main");
		}else {
			return new ModelAndView("signup"); 
		}
	}
	
	@PostMapping("/signup")//회원가입하기
	public int createMember(@RequestBody Member member) {
		
			memberService.createMember(member);//DB에 이름,이메일,비밀번호,닉네임을 입력. 성공하면 1을 반환 
			int mem_id = memberService.getMember(member.getEmail()).getId();//DB의 member테이블에 새 레코드가 추가되면 pk인 id를 가져옴 
			
			for(int member_genre:member.getMember_genre()) {//회원가입시 선택한 선호 장르 갯수와 똑같은 횟수로 DB에 입력
				memberService.setMemberGenre(member_genre,mem_id );
			}
			return 1;//signup.js 의 fetch의 결과로 200을 반환
		
	}
	/*
	@PostMapping("/signup")//회원가입하기
	public ResponseEntity<String> createMember(@RequestBody Member member) {
		
		if(memberService.createMember(member)==1){//DB에 이름,이메일,비밀번호,닉네임을 입력. 성공하면 1을 반환 
			int mem_id = memberService.getMember(member.getEmail()).getId();//DB의 member테이블에 새 레코드가 추가되면 pk인 id를 가져옴 
			
			for(int member_genre:member.getMember_genre()) {//회원가입시 선택한 선호 장르 갯수와 똑같은 횟수로 DB에 입력
				memberService.setMemberGenre(member_genre,mem_id );
			
			return new ResponseEntity<>("success",HttpStatus.OK);//signup.js 의 fetch의 결과로 200을 반환
			}
		}else {//createMember()가 실패하면 0을 반환
			return new ResponseEntity<>("fail",HttpStatus.BAD_REQUEST);//signup.js 의 fetch의 결과로 400을 반환
		}
	*/
	
}
