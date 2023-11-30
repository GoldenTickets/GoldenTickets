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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.goldenticket.DTO.Article;
import com.goldenticket.DTO.Reply;
import com.goldenticket.mapper.BoardMapper;
import com.goldenticket.service.BoardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="BoardController",description="게시판 관련 메서드입니다.")
@RestController
public class BoardController {
	
	
	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private BoardService boardService;
	
	@Operation(summary="게시판",description="게시판으로 이동합니다. 게시글 목록을 보여줍니다. 게시글 10개당 한 페이지로 페이징처리되고, 10,15,20개씩 볼수있도록 변경가능합니다."
			+ "게시글의 제목,제목과 닉네임,닉네임을 검색할 수 있습니다.")
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
	
	@Operation(summary="게시물 조회",description="게시글의 제목을 클릭하면 게시글을 볼수있게 해당 게시물페이지로 이동합니다. 해당 게시물의 작성자가 열람했을때만 "
			+ "게시물에 대한 수정,삭제 버튼이 생깁니다. 게시글의 조회수가 1 증가합니다. 댓글목록을 가져옵니다. 댓글은 10개 묶음으로 페이징처리됩니다.")
	@GetMapping("/articles/{id}")
	public ModelAndView getArticle(@RequestParam(defaultValue = "1") int page, @PathVariable("id") int id){
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
	
	@Operation(summary="게시글 작성 페이지",description="게시글을 작성할 수 있게 게시물 작성 페이지로 이동합니다.")
	@GetMapping("/new-article")
	public ModelAndView getNewArticlePage(){
		ModelAndView mav = new ModelAndView("newArticle");
		return mav;
	}
	
	@Operation(summary="게시글 작성",description="게시물 작성 페이지에서 게시물을 작성하면 DB에 저장 후, 해당 게시물을 보여주는 페이지로 이동합니다. 로그인이되어있지않다면 오른쪽에서 로그인섹션이 열립니다.")
	@Transactional(rollbackFor=Exception.class) //서비스로 뺄 방법 강구하기
	@PostMapping("/articles")
	public ResponseEntity<String> createArticle(@RequestBody Article article
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
	
	@Operation(summary="게시글 수정 페이지",description="게시글을 수정할 수 있게 게시물 수정 페이지로 이동합니다. 작성했던 글의 제목,내용을 불러와집니다.")
	//글 수정하기 페이지로 이동
	@GetMapping("/update-article/{id}")
	public ModelAndView getUpdateArticlePage(@PathVariable int id,HttpSession session){
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
	
		
	}
	
	@Operation(summary="게시글 수정",description="회원이 작성한 게시물을 수정하는 기능입니다. 로그인을 하지않았거나 해당 게시글의 작성자가 아닐 경우 수정,삭제 버튼이 "
			+ "없지만, 만약 사용자가 버튼 클릭이 아닌 방식으로 수정을 요청할 경우를 대비해 세션에 저장된 회원의 id와 비교합니다. 로그인을 하지않았다면 오른쪽에서 로그인섹션이 열립니다."
			+ "세션에 저장된 사용자의 id와 DB에 저장된 해당게시글의 작성자의 id가 일치할경우에만 DB에 저장된 레코드의 수정이 성공적으로 수행됩니다."
			+ "수정이 성공적으로 완료되면 DB에 수정된 정보를 업데이트 후, 해당 게시물을 보여주는 페이지로 이동합니다.")
	@Transactional(rollbackFor=Exception.class) //서비스로 뺄 방법 강구하기
	@PutMapping("/articles/{article_id}")
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
	
	@Operation(summary="게시글 삭제",description="회원이 작성한 게시물을 삭제하는 기능입니다. 로그인을 하지않았거나 해당 게시글의 작성자가 아닐 경우 수정,삭제"
			+ "버튼이 없지만, 만약 사용자가 버튼 클릭이 아닌 방식으로 삭제를 요청할 경우를 대비해 세션에 저장된 회원의 id와 비교합니다. 로그인을 하지않았다면 오른쪽에서 로그인섹션이 열립니다."
			+ "세션에 저장된 사용자의 id와 DB에 저장된 해당게시글의 작성자의 id가 일치할경우에만 DB에 저장된 레코드의 삭제가 성공적으로 수행됩니다.")
	@Transactional(rollbackFor=Exception.class) //서비스로 뺄 방법 강구하기
	@DeleteMapping("/articles/{article_id}")
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
	
	@Operation(summary="댓글 작성",description="게시물에 댓글을 작성하는 기능입니다.로그인이되어있지않다면 오른쪽에서 로그인섹션이 열립니다.")
	@Transactional(rollbackFor=Exception.class) //서비스로 뺄 방법 강구하기
	@PostMapping("/articles/reply")
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
	
	@Operation(summary="댓글 삭제",description="회원의 댓글을 삭제하는 기능입니다. 로그인을 하지않았거나 해당 댓글의 작성자가 아닐 경우 삭제 버튼이 없지만, "
			+ "만약 사용자가 버튼 클릭이 아닌 방식으로 삭제를 요청할 경우를 대비해 세션에 저장된 회원의 id와 비교합니다."
			+ "세션에 저장된 사용자의 id와 DB에 저장된 해당댓글의 작성자의 id가 일치할경우에만 DB에 저장된 레코드의 삭제가 성공적으로 수행됩니다.")
	@Transactional(rollbackFor=Exception.class) //서비스로 뺄 방법 강구하기
	@DeleteMapping("/articles/reply/{reply_id}")
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
