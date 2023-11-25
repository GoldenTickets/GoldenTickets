package com.goldenticket.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.goldenticket.DTO.Article;
import com.goldenticket.DTO.Reply;
import com.goldenticket.mapper.BoardMapper;
import com.goldenticket.service.BoardService;

@RestController
@RequestMapping("/board")
public class BoardController {
	
	
	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private BoardService boardService;
	
	@GetMapping("")
	public ModelAndView getAll(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "0") int category, @RequestParam(defaultValue = "10") int pagesize, @RequestParam(name = "keyword", required = false) String keyword, @RequestParam(name = "subject", defaultValue = "none") String subject){ // page = 현재페이지, pageSize도 나중에 정할 수 있게 바꾸기
		
		ModelAndView mav = new ModelAndView("articleList");
		int startRow = (page-1)*pagesize;
		RowBounds rowBounds = new RowBounds(startRow, pagesize); // 페이징 처리
		List<Article> articles;
		int totalArticles;
		
		if (category == 0) { // 카테고리 전체
			if (subject.equals("title")) { // 제목으로 검색
				articles = boardMapper.getByTitle(rowBounds, keyword);
				totalArticles = boardMapper.totalArticlesByTitle(keyword);
			}else if (subject.equals("nickname")){ // 닉네임으로 검색
				articles = boardMapper.getByNickname(rowBounds, keyword);
				totalArticles = boardMapper.totalArticlesByNickname(keyword);
			}else if (subject.equals("both")) { // 제목 + 닉네임으로 검색
				articles = boardMapper.getByTitleAndNickname(rowBounds, keyword);
				totalArticles = boardMapper.totalArticlesByTitleAndNickname(keyword);
			}else { // 전체 조회
				articles = boardMapper.getAll(rowBounds);
				totalArticles = boardMapper.totalArticles();
			}
		}else { // 카테고리로 조회
			articles = boardMapper.getAllByCategory(rowBounds, category);
			totalArticles = boardMapper.totalArticlesByCategory(category);
		}
		
		mav.addObject("pagesize", pagesize);
		mav.addObject("category", category);
		mav.addObject("articles", articles);
		
		int totalPages = (int) Math.ceil((double) totalArticles / pagesize); // 전체 페이지 수 구하기
		mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);
		
		return mav;
	}
	/*
	@GetMapping("/search")
	public ModelAndView getBySearch(@RequestParam(name = "keyword", required = false) String keyword, @RequestParam(name = "subject", required = false) String subject, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pagesize) {
		
		ModelAndView mav = new ModelAndView("articleList");
		int startRow = (page-1)*pagesize;
		RowBounds rowBounds = new RowBounds(startRow, pagesize); // 페이징 처리
		List<Article> articles;
		int totalArticles;
		
		if (subject.equals("title")) {
			articles = boardMapper.getByTitle(rowBounds, keyword);
			totalArticles = boardMapper.totalArticlesByTitle(keyword);
		}else if (subject.equals("nickname")){
			articles = boardMapper.getByNickname(rowBounds, keyword);
			totalArticles = boardMapper.totalArticlesByNickname(keyword);
		}else if (subject.equals("both")) {
			articles = boardMapper.getByTitleAndNickname(rowBounds, keyword);
			totalArticles = boardMapper.totalArticlesByTitleAndNickname(keyword);
		}else {
			articles = boardMapper.getAll(rowBounds);
			totalArticles = boardMapper.totalArticles();
		}
		
		mav.addObject("pagesize", pagesize);
		mav.addObject("articles", articles);
		
		int totalPages = (int) Math.ceil((double) totalArticles / pagesize); // 전체 페이지 수 구하기
		mav.addObject("currentPage", page);
		mav.addObject("totalPages", totalPages);
		
		return mav;
	}
	*/
	@GetMapping("/{id}")
	public ModelAndView getById(@RequestParam(defaultValue = "1") int page, @PathVariable("id") int id){
		boardService.updateHit(id);//조회수 1 증가
		
		int pageSize = 10;
		int startRow = (page-1)*pageSize;
		RowBounds rowBounds = new RowBounds(startRow, pageSize); // 페이징 처리
		
		ModelAndView mav = new ModelAndView("article");
		Article article = boardMapper.getById(id);
		List<Reply> replies = boardMapper.getByArticle_id(id, rowBounds);
		mav.addObject("article", article);
		mav.addObject("replies", replies);
		System.out.println(article);
		
		int totalReplies = boardMapper.getTotalreply(id);
		int totalPages = (int) Math.ceil((double) totalReplies / pageSize); // 전체 페이지 수 구하기
		mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);
        mav.addObject("totalReplies", totalReplies);
        
		return mav;
	}
	
	@GetMapping("/write")
	public ModelAndView writeArticle(){
		ModelAndView mav = new ModelAndView("newArticle");
		return mav;
	}
	
	@Transactional(rollbackFor=Exception.class) //서비스로 뺄 방법 강구하기
	@PostMapping("/write")
	public ResponseEntity<String> post(@RequestBody Article article
									   ,HttpSession session){
		try {
			Object session_id = session.getAttribute("id");
			if(session_id!=null) {//로그인 상태
				int mem_id=(int)session_id;
				article.setMem_id(mem_id);
				int result = boardService.createArticle(article);
				if(result == 1) {
					return new ResponseEntity<>("success",HttpStatus.OK);
				}else {
					return new ResponseEntity<>("fail",HttpStatus.OK);
				}
			}else {
				return new ResponseEntity<>("needLogin",HttpStatus.OK);
			}
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("error",HttpStatus.BAD_REQUEST);
		}
	}
	
	//글 수정하기 페이지로 이동
	@GetMapping("/update/{id}")
	public ModelAndView update(@PathVariable int id,HttpSession session){
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
				return new ModelAndView("redirect:/board");
			}
		}else {
			return new ModelAndView("redirect:/board");
		}
	
		
	}
	
	
	@Transactional(rollbackFor=Exception.class) //서비스로 뺄 방법 강구하기
	@PutMapping("/update/{article_id}")
	public ResponseEntity<String> updateArticle(@PathVariable int article_id
									   			,@RequestBody Article article
												,HttpSession session){
		try {
			System.out.println("article_id=>"+article_id);
			Object session_id = session.getAttribute("id");
			if(session_id!=null) {//로그인 상태
				int mem_id=(int)session_id;

				if(mem_id == boardService.getArticleById(article_id).getMem_id()){//글 삭제하기전 세션의 아이디와 글의 mem_id가 일치하는지 확인
					article.setId(article_id);
					System.out.println("article=>"+article);
					int result = boardService.updateArticle(article);
					if(result == 1) {
						return new ResponseEntity<>("success",HttpStatus.OK);
					}else {
						return new ResponseEntity<>("fail",HttpStatus.OK);
					}//삭제성공
				}else{//세션과 일치하지 않으면
					return new ResponseEntity<>("loginwrong",HttpStatus.OK);
				}
			}else {
				return new ResponseEntity<>("needLogin",HttpStatus.OK);
			}	
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("error",HttpStatus.BAD_REQUEST);
		}
	}
	
	@Transactional(rollbackFor=Exception.class) //서비스로 뺄 방법 강구하기
	@DeleteMapping("/delete/{article_id}")
	public ResponseEntity<String> deleteArticle(@PathVariable int article_id
									   			,HttpSession session){
		try {
			System.out.println("article_id=>"+article_id);
			Object session_id = session.getAttribute("id");
			if(session_id!=null) {//로그인 상태
				int mem_id=(int)session_id;
				if(mem_id == boardService.getArticleById(article_id).getMem_id()){//글 삭제하기전 세션의 아이디와 글의 mem_id가 일치하는지 확인
					int result = boardService.deleteArticle(article_id);
					if(result == 1) {
						return new ResponseEntity<>("success",HttpStatus.OK);
					}else {
						return new ResponseEntity<>("fail",HttpStatus.OK);
					}//삭제성공
				}else{//세션과 일치하지 않으면
					return new ResponseEntity<>("loginwrong",HttpStatus.OK);
				}
			}else {
				return new ResponseEntity<>("needLogin",HttpStatus.OK);
			}	
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("error",HttpStatus.BAD_REQUEST);
		}
	}
	
	@Transactional(rollbackFor=Exception.class) //서비스로 뺄 방법 강구하기
	@PostMapping("/replywrite")
	public ResponseEntity<String> createReply(@RequestBody Reply reply
									   ,HttpSession session){
		try {
			Object session_id = session.getAttribute("id");
			if(session_id!=null) {//로그인 상태
				int mem_id=(int)session_id;
				reply.setMem_id(mem_id);
				int result = boardService.createReply(reply);
				if(result == 1) {
					return new ResponseEntity<>("success",HttpStatus.OK);
				}else {
					return new ResponseEntity<>("fail",HttpStatus.OK);
				}
			}else {
				return new ResponseEntity<>("needLogin",HttpStatus.OK);
			}
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("error",HttpStatus.BAD_REQUEST);
		}
	}
		
	@Transactional(rollbackFor=Exception.class) //서비스로 뺄 방법 강구하기
	@DeleteMapping("/deleteReply/{reply_id}")
	public ResponseEntity<String> deleteReply(@PathVariable int reply_id
									   			,HttpSession session){
		try {
			System.out.println("reply_id=>"+reply_id);
			Object session_id = session.getAttribute("id");
			if(session_id!=null) {//로그인 상태
				int mem_id=(int)session_id;
				if(mem_id == boardService.confirmIdOfReply(reply_id)){//글 삭제하기전 세션의 아이디와 댓글의 mem_id가 일치하는지 확인
					int result = boardService.deleteReply(reply_id);
					if(result == 1) {
						return new ResponseEntity<>("success",HttpStatus.OK);
					}else {
						return new ResponseEntity<>("fail",HttpStatus.OK);
					}//삭제성공
				}else{//세션과 일치하지 않으면
					return new ResponseEntity<>("loginwrong",HttpStatus.OK);
				}
			}else {
				return new ResponseEntity<>("needLogin",HttpStatus.OK);
			}	
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("error",HttpStatus.BAD_REQUEST);
		}
	}
	
}
