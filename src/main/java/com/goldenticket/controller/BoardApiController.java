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
import com.goldenticket.service.BoardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="게시판 API",description="게시판 테이블과 관련된 API 입니다.")

@RestController
public class BoardApiController {
	
	@Autowired
	private BoardService boardService;
	
		@Operation(summary="게시글 작성",description="회원이 작성한 게시물을 저장하는 기능입니다. 게시물 작성 페이지에서 게시물을 "
			+ "작성하면 DB에 저장 후, 해당 게시물을 보여주는 페이지로 이동합니다. 로그인이되어있지않다면 오른쪽에서 로그인 섹션이 열립니다.")
		@Transactional(rollbackFor=Exception.class) 
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
		
	
	@Operation(summary="게시글 수정",description="회원이 작성한 게시물을 수정하는 기능입니다. 로그인을 하지않았거나 해당 게시글의 작성자가 아닐 경우 수정,삭제 버튼이 "
			+ "없지만, 만약 사용자가 버튼클릭이 아닌 방식으로 수정을 요청할 경우를 대비해 세션에 저장된 회원의 id와 비교합니다. 로그인을 하지않았다면 오른쪽에서 로그인섹션이 열립니다."
			+ "세션에 저장된 사용자의 id와 DB에 저장된 해당게시글의 작성자의 id가 일치할 경우에만 DB에 저장된 레코드의 수정이 성공적으로 수행됩니다."
			+ "수정이 성공적으로 완료되면 DB에 수정된 정보를 업데이트 후, 해당 게시물을 보여주는 페이지로 이동합니다.")
	@Transactional(rollbackFor=Exception.class) //서비스로 뺄 방법 강구하기
	@PutMapping("/articles/{article_id}")
	public ResponseEntity<String> updateArticle(@Parameter(description = "게시글의 primary key인 id 컬럼입니다.") @PathVariable int article_id,
												@Parameter(description = "게시글 수정페이지에서 입력한 category,title,content")@RequestBody Article article,
												HttpSession session){
		try {
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
	public ResponseEntity<String> deleteArticle(@Parameter(description = "게시글의 primary key인 id 컬럼입니다.") @PathVariable int article_id
									   			,HttpSession session){
		try {
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
	
	@Operation(summary="댓글 작성",description="게시물에 댓글을 작성하는 기능입니다.로그인이 되어있지않다면 오른쪽에서 로그인 섹션이 열립니다.")
	@Transactional(rollbackFor=Exception.class) //서비스로 뺄 방법 강구하기
	@PostMapping("/articles/reply")
	public ResponseEntity<String> createReply(@Parameter(description = "입력한 댓글의 content입니다.") @RequestBody Reply reply
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
	public ResponseEntity<String> deleteReply(@Parameter(description = "입력한 댓글의 primary key인 id입니다.") @PathVariable int reply_id
									   		  ,HttpSession session){
		try {
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
