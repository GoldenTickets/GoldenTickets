package com.goldenticket.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.goldenticket.DTO.Article;
import com.goldenticket.DTO.Reply;
import com.goldenticket.service.BoardService;

import io.swagger.v3.oas.annotations.Operation;

@Controller
public class BoardViewController {

	@Autowired
	private BoardService boardService;
	
	//게시판으로 이동합니다. 게시글 목록을 보여줍니다. 게시글 10개당 한 페이지로 페이징처리되고, 10,15,20개씩 볼수있도록 변경가능합니다.
	//게시글의 제목,제목과 닉네임,닉네임을 검색할 수 있습니다.
	@GetMapping("/articles")
	public ModelAndView getArticles(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "0") int category, 
			@RequestParam(defaultValue = "10") int pagesize, 
			@RequestParam(name = "keyword", required = false) String keyword, 
			@RequestParam(name = "subject", defaultValue = "none") String subject){ // page = 현재페이지, pageSize도 나중에 정할 수 있게 바꾸기
		
		ModelAndView mav = new ModelAndView("articleList");
		int startRow = (page-1)*pagesize;
		RowBounds rowBounds = new RowBounds(startRow, pagesize); // 페이징 처리
		List<Article> articles;
		int totalArticles;
		
		try {
			if (category == 0) { // 카테고리 전체
				if (subject.equals("title")) { // 제목으로 검색
					articles = boardService.getByTitle(rowBounds, keyword);
					totalArticles = boardService.totalArticlesByTitle(keyword);
				}else if (subject.equals("nickname")){ // 닉네임으로 검색
					articles = boardService.getByNickname(rowBounds, keyword);
					totalArticles = boardService.totalArticlesByNickname(keyword);
				}else if (subject.equals("both")) { // 제목 + 닉네임으로 검색
					articles = boardService.getByTitleAndNickname(rowBounds, keyword);
					totalArticles = boardService.totalArticlesByTitleAndNickname(keyword);
				}else { // 전체 조회
					articles = boardService.getAll(rowBounds);
					totalArticles = boardService.totalArticles();
				}
			}else { // 카테고리로 조회
				articles = boardService.getAllByCategory(rowBounds, category);
				totalArticles = boardService.totalArticlesByCategory(category);
			}
			
			mav.addObject("pagesize", pagesize);
			mav.addObject("category", category);
			mav.addObject("articles", articles);
			
			int totalPages = (int) Math.ceil((double) totalArticles / pagesize); // 전체 페이지 수 구하기
			mav.addObject("currentPage", page);
	        mav.addObject("totalPages", totalPages);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return mav;
	}
	
	//게시글의 제목을 클릭하면 게시글을 볼수있게 해당 게시물페이지로 이동합니다. 해당 게시물의 작성자가 열람했을때만 
	//게시물에 대한 수정,삭제 버튼이 생깁니다. 게시글의 조회수가 1 증가합니다. 댓글목록을 가져옵니다. 댓글은 10개 묶음으로 페이징처리됩니다.
	@GetMapping("/articles/{id}")
	public ModelAndView getArticle(@RequestParam(defaultValue = "1") int page, @PathVariable("id") int id){
		try {
			boardService.updateHit(id);//조회수 1 증가
			
			int pageSize = 10;
			int startRow = (page-1)*pageSize;
			RowBounds rowBounds = new RowBounds(startRow, pageSize); // 페이징 처리
			
			ModelAndView mav = new ModelAndView("article");
			Article article = boardService.getById(id);
			List<Reply> replies = boardService.getByArticle_id(id, rowBounds);
			mav.addObject("article", article);
			mav.addObject("replies", replies);
			
			int totalReplies = boardService.getTotalreply(id);
			int totalPages = (int) Math.ceil((double) totalReplies / pageSize); // 전체 페이지 수 구하기
			mav.addObject("currentPage", page);
	        mav.addObject("totalPages", totalPages);
	        mav.addObject("totalReplies", totalReplies);
	        
	        return mav;
		}catch(Exception e) {
			e.printStackTrace();
			return new ModelAndView("article");
		}
	}
	
	@Operation(summary="게시글 작성 페이지",description="게시글을 작성할 수 있게 게시물 작성 페이지로 이동합니다.")
	@GetMapping("/new-article")
	public ModelAndView getNewArticlePage(){
		ModelAndView mav = new ModelAndView("newArticle");
		return mav;
	}
	
	
	//게시글을 수정할 수 있게 게시물 수정 페이지로 이동합니다. 작성했던 글의 제목,내용을 불러와집니다.
	@GetMapping("/update-article/{id}")
	public ModelAndView getUpdateArticlePage(@PathVariable int id,HttpSession session){
		try {
			Object session_id = session.getAttribute("id");
			int mem_id;
			if(session_id!=null) {
				mem_id=(int)session_id;
				Article article = boardService.getArticleById(id);
				if(mem_id == boardService.getArticleById(id).getMem_id()) {
					ModelAndView mav = new ModelAndView("updateArticle");
					mav.addObject("article",article);
					return mav;
				}else {
					return new ModelAndView("redirect:/articles");
				}
			}else {
				return new ModelAndView("redirect:/articles");
			}
		}catch(Exception e) {
			e.printStackTrace();
			return new ModelAndView("redirect:/articles");
		}
	}
	
	
	
	
}
