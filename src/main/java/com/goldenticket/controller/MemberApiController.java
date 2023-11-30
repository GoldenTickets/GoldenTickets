package com.goldenticket.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.goldenticket.DTO.Article;
import com.goldenticket.DTO.Member;
import com.goldenticket.DTO.Movie;
import com.goldenticket.DTO.Reply;
import com.goldenticket.DTO.Review;
import com.goldenticket.mapper.MemberMapper;
import com.goldenticket.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="MemberController",description="회원 관련 메서드입니다.")
@RestController
public class MemberApiController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MemberMapper memberMapper;
	
	//로그인 기능 ( login페이지나 offcanvas에서 로그인 모두 포함 )
	@Operation(summary="로그인",description="로그인 기능입니다.")
	@PostMapping("/login")
	public ResponseEntity<String> login(@Parameter(description = "회원의 primary key인 email 컬럼입니다.") @RequestParam("email") String email,
										@Parameter(description = "회원의 password 컬럼입니다.")@RequestParam("password") String password,
			 					  HttpSession session) throws Exception {
		try {
			int result = memberService.loginCheck(email,password);
			if(result==1) {//로그인 아이디비밀번호 일치할경우
				int membeno=memberService.getMember(email).getId();
				String nickname=memberService.getMember(email).getNickname();

				session.setAttribute("id",membeno);
				session.setAttribute("nickname",nickname);
				
				return new ResponseEntity<>("ok",HttpStatus.OK);//로그인 성공할 경우 OK실행
			}else {//아이디 비밀번호 불일치일 경우 result = 0 
				return new ResponseEntity<>("fail",HttpStatus.BAD_REQUEST);//로그인 실패할경우 400 반환 
			}	

		}catch(Exception e) {
			return new ResponseEntity<>("ok",HttpStatus.BAD_REQUEST);//로그인 실패할경우 400 반환 
		}
	}

	//로그아웃
	@Operation(summary="로그아웃",description="로그아웃 기능입니다.")
	@GetMapping("/logout")
	public ResponseEntity<String> logout(HttpSession session){
		session.invalidate();
		return new ResponseEntity<>("logout",HttpStatus.OK); 
	}

	//회원가입하기
	@Operation(summary="회원가입",description="회원가입 기능입니다.")
	@PostMapping("/signup")
	public ResponseEntity<String> createMember(@Parameter(description = "회원가입때 입력하는 회원정보입니다.") @RequestBody Member member) {
		try{
			memberService.createMember(member);//DB에 이름,이메일,비밀번호,닉네임을 입력. 성공하면 1을 반환 
			int mem_id = memberService.getMember(member.getEmail()).getId();//DB의 member테이블에 새 레코드가 추가되면 pk인 id를 가져옴 
			for(int member_genre:member.getMember_genre()) {//회원가입시 선택한 선호 장르 갯수와 똑같은 횟수로 DB에 입력
				memberService.setMemberGenre(member_genre,mem_id );
			}
			return new ResponseEntity<>("success",HttpStatus.OK);//signup.js 의 fetch의 결과로 200을 반환
		}catch(Exception e) {
			return new ResponseEntity<>("fail",HttpStatus.BAD_REQUEST);//signup.js 의 fetch의 결과로 400을 반환
		}
		

	}
	
	//마이페이지 회원 수정
	@Operation(summary="회원정보수정",description="마이페이지에서 회원의 정보를 수정하는기능입니다.")
	@Transactional(rollbackFor=Exception.class) //서비스로 뺄 방법 강구하기
	@PutMapping("/mypage")
	public ResponseEntity<String> updateMember(@Parameter(description = "회원수정때 입력하는 회원정보입니다.")@RequestBody Member member
											   ,HttpSession session){
			try {	
				int id=(int)session.getAttribute("id");
				member.setId(id);
				System.out.println("member=>"+member);
				System.out.println("member.getMember_genre()=>"+member.getMember_genre());
				int result = memberService.updateMember(member,member.getMember_genre());
				
				if(result == 1) {//수정 성공
					session.setAttribute("nickname", member.getNickname());//수정에 성공하고 나면 세션에 저장된 닉네임 변경
					return new ResponseEntity<>("success",HttpStatus.OK);
				}else {//수정 실패
					return new ResponseEntity<>("fail",HttpStatus.OK);
				}
			}catch(Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>("error",HttpStatus.BAD_REQUEST);
		}
	}
	
}
