package com.goldenticket.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.goldenticket.DTO.Article;
import com.goldenticket.DTO.Member;
import com.goldenticket.DTO.Movie;
import com.goldenticket.DTO.Reply;
import com.goldenticket.DTO.Review;
import com.goldenticket.mapper.MemberMapper;
import com.goldenticket.service.MemberService;

@Controller
public class MemberViewController {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MemberMapper memberMapper;
	
	//로그인 페이지로 이동합니다. 이미 로그인이 되어있다면 로그인페이지가 아닌 메인페이지로 이동합니다.
	@GetMapping("/login")
	public ModelAndView getLoginPage(HttpSession session){
		
		if(session.getAttribute("id") == null) {//로그인이 되어있지않다면
			return new ModelAndView("login");
		}else {
			return new ModelAndView("redirect:/");//로그인이 이미 되어있다면
		}
		
	}

	//회원가입 페이지로 이동합니다.
	@GetMapping("/signup")
	public ModelAndView getSignupPage(HttpSession session){
		if(session.getAttribute("nickname")!=null) {
			return new ModelAndView("main");
		}else {
			return new ModelAndView("signup"); 
		}
	}
	
	//마이페이지로 이동합니다. 로그인이 되어있지않다면 메인으로 이동합니다.
	@GetMapping("/mypage")
	public ModelAndView getMypage(HttpSession session) {
		try {
			int mem_id;
			Object session_id = session.getAttribute("id");
			if(session_id != null) {//로그인이 되어있으면 
				mem_id = (int)session_id;
				ModelAndView mav = new ModelAndView("mypage");
				int id = (int) session.getAttribute("id");
				Member member = memberService.getMemberinfo(mem_id);//회원의 기존 정보
					
				//회원이 선호하는 장르 목록
				List<Integer> mem_genre_list = memberService.getMemGenreList(mem_id);
					
				mav.addObject("member", member);
				mav.addObject("mem_genre_list",mem_genre_list);
					
				return mav;
			}else {//로그인이 되어있지 않으면 main으로 
				return new ModelAndView("redirect:/");
			}
		}catch(Exception e) {
			e.printStackTrace();
		return new ModelAndView("redirect:/");
		}

	}
	
	//회원이 북마크한 영화들의 목록을 보여줍니다. 영화포스터를 클릭하면 해당 영화페이지로 이동합니다.
	@GetMapping("/mypage/bookmark")
	public ModelAndView getMyBookmark(@RequestParam(defaultValue = "1") int page, HttpSession session) {
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
	
	//회원이 작성한 리뷰의 목록을 보여줍니다. 제목을 클릭하면 해당 영화페이지로 이동합니다. 목록의 페이징은 10개로 기본설정되어있습니다
	@GetMapping("/mypage/review")
	public ModelAndView getMyReview(@RequestParam(defaultValue = "1") int page, HttpSession session) {
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
	
	//회원이 작성한 게시글의 목록을 보여줍니다. 회원이 작성한 게시글을 삭제할수있으며, 게시글의 제목을 클릭하면 해당 게시글로 이동합니다.
	@GetMapping("/mypage/article")
	public ModelAndView getMyArticle(@RequestParam(defaultValue = "1") int page, HttpSession session) {
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
	
	//회원이 작성한 게시글의 목록을 보여줍니다. 회원이 작성한 게시글을 삭제할수있으며, 게시글의 제목을 클릭하면 해당 게시글로 이동합니다.
	@GetMapping("/mypage/reply")
	public ModelAndView getMyReply(@RequestParam(defaultValue = "1") int page, HttpSession session) {
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
