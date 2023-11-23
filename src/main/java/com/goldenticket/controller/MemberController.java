package com.goldenticket.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

@RestController
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MemberMapper memberMapper;
	
	//로그인페이지로 이동
	@GetMapping("/login")
	public ModelAndView loginPage(HttpSession session){
		
		if(session.getAttribute("id") == null) {//로그인이 되어있지않다면
			return new ModelAndView("/login");
		}else {
			return new ModelAndView("redirect:/");//로그인이 이미 되어있다면
		}
		
	}
	
	//로그인 기능 ( login페이지나 offcanvas에서 로그인 모두 포함
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestParam("email") String email,
			 					  @RequestParam("password") String password,
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

		}catch(Exception e) {//ㅇ
			return new ResponseEntity<>("ok",HttpStatus.BAD_REQUEST);//로그인 실패할경우 400 반환 
		}
	}

	//로그아웃
	@GetMapping("/logout")
	public ResponseEntity<String> logout(HttpSession session){
		session.invalidate();
		return new ResponseEntity<>("logout",HttpStatus.OK); 
	}

	//회원가입페이지로 이동
	@GetMapping("/signup")
	public ModelAndView signup(HttpSession session){
		if(session.getAttribute("nickname")!=null) {
			return new ModelAndView("main");
		}else {
			return new ModelAndView("signup"); 
		}
	}

	//회원가입하기
	@PostMapping("/signup")
	public ResponseEntity<String> createMember(@RequestBody Member member) {
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
	
	@GetMapping("/mypage")
	public ModelAndView mypage(HttpSession session) {
		ModelAndView mav = new ModelAndView("mypage");
		int id = (int) session.getAttribute("id");
		Member member = memberService.getMemberinfo(id);
		mav.addObject("member", member);
		return mav;
	}
	
	@GetMapping("/mypage/bookmark")
	public ModelAndView bookmark(@RequestParam(defaultValue = "1") int page, HttpSession session) {
		ModelAndView mav = new ModelAndView("mypage_bookmark");
		int id = (int) session.getAttribute("id");
		int pageSize = 15;
		int startRow = (page-1)*pageSize;
		RowBounds rowBounds = new RowBounds(startRow, pageSize); // 페이징 처리
		
		int totalMovies = memberMapper.getTotalbookmark(id);
		int totalPages = (int) Math.ceil((double) totalMovies / pageSize); // 전체 페이지 수 구하기
		
		mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);

		List<Movie> movies = memberMapper.getBookmark(rowBounds, id);
		
		mav.addObject("movies", movies);
		return mav;
	}
	
	@GetMapping("/mypage/review")
	public ModelAndView review(@RequestParam(defaultValue = "1") int page, HttpSession session) {
		ModelAndView mav = new ModelAndView("mypage_review");
		int id = (int) session.getAttribute("id");
		int pageSize = 10;
		int startRow = (page-1)*pageSize;
		RowBounds rowBounds = new RowBounds(startRow, pageSize); // 페이징 처리
		
		int totalMovies = memberMapper.getMytotalreviews(id);
		int totalPages = (int) Math.ceil((double) totalMovies / pageSize); // 전체 페이지 수 구하기
		
		mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);

		List<Review> reviews = memberMapper.getMyreviews(rowBounds, id);
		
		mav.addObject("reviews", reviews);
		return mav;
	}
	
	@GetMapping("/mypage/article")
	public ModelAndView article(@RequestParam(defaultValue = "1") int page, HttpSession session) {
		ModelAndView mav = new ModelAndView("mypage_article");
		int id = (int) session.getAttribute("id");
		int pageSize = 10;
		int startRow = (page-1)*pageSize;
		RowBounds rowBounds = new RowBounds(startRow, pageSize); // 페이징 처리
		
		int totalMovies = memberMapper.getMytotalarticles(id);
		int totalPages = (int) Math.ceil((double) totalMovies / pageSize); // 전체 페이지 수 구하기
		
		mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);

		List<Article> articles = memberMapper.getMyarticles(rowBounds, id);
		
		mav.addObject("articles", articles);
		return mav;
	}
	
	@GetMapping("/mypage/reply")
	public ModelAndView reply(@RequestParam(defaultValue = "1") int page, HttpSession session) {
		ModelAndView mav = new ModelAndView("mypage_reply");
		int id = (int) session.getAttribute("id");
		int pageSize = 10;
		int startRow = (page-1)*pageSize;
		RowBounds rowBounds = new RowBounds(startRow, pageSize); // 페이징 처리
		
		int totalMovies = memberMapper.getMytotalreplies(id);
		int totalPages = (int) Math.ceil((double) totalMovies / pageSize); // 전체 페이지 수 구하기
		
		mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);

		List<Reply> replies = memberMapper.getMyreplies(rowBounds, id);
		
		mav.addObject("replies", replies);
		return mav;
	}
}
