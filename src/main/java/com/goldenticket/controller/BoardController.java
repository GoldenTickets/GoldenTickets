package com.goldenticket.controller;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
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
	public ModelAndView getAll(@RequestParam(defaultValue = "1") int page) { // page = 현재페이지, pageSize도 나중에 정할 수 있게 바꾸기
		
		ModelAndView mav = new ModelAndView("articleList");
		int pageSize = 10;
		int startRow = (page-1)*pageSize;
		RowBounds rowBounds = new RowBounds(startRow, pageSize); // 페이징 처리
		List<Article> articles = boardMapper.getAll(rowBounds);
		mav.addObject("articles", articles);
		
		int totalArticles = boardMapper.totalArticles();
		int totalPages = (int) Math.ceil((double) totalArticles / pageSize); // 전체 페이지 수 구하기
		mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);
		
		return mav;
	}
	
	@GetMapping("/{id}")
	public ModelAndView getById(@PathVariable("id") int id) {
		boardService.updateHit(id);//조회수 1 증가
		ModelAndView mav = new ModelAndView("article");
		Article article = boardMapper.getById(id);
		List<Reply> replies = boardMapper.getByArticle_id(id);
		mav.addObject("article", article);
		mav.addObject("replies", replies);
		return mav;
	}
	
	@GetMapping("/write")
	public ModelAndView writeArticle() {
		ModelAndView mav = new ModelAndView("newArticle");
		return mav;
	}
	
	@Transactional(rollbackFor=Exception.class) //서비스로 뺄 방법 강구하기
	@PostMapping("")
	public Article post(@RequestBody Article article) {
		System.out.println(article);
		int cid = article.getCategory_id();
		boardMapper.save(article);
		article = boardMapper.getNewId();
		article.setCategory_id(cid);
		boardMapper.save2(article);
		return article;
	}
	
	//글 수정하기 페이지로 이동
	@GetMapping("/update/{id}")
	public ModelAndView update(@PathVariable int id) {
		
		Article article = boardService.getArticleById(id);
		ModelAndView mav = new ModelAndView("update/"+id);
		return mav;
	}
	
	
	@Transactional(rollbackFor=Exception.class)
	@PutMapping("")
	public Article update(@RequestBody Article article) {
		boardService.updateArticle(article);
		//boardService.acUpdate(article); // 조건문 넣기
		return article;
	}
	
	//글 삭제하기
	@Transactional(rollbackFor=Exception.class)
	@DeleteMapping("/delete/{id}")
	public ModelAndView delete(@PathVariable int id) {
		//boardMapper.deleteac(article);
		boardService.deleteArticle(id);
		return new ModelAndView("redirect:/board");
	}
	
	@PostMapping("/submitreply/{id}")
	public ModelAndView createReply(@RequestBody Reply reply,@PathVariable int id) {
		boardService.createReply(reply);
		return new ModelAndView("redirect:/board/"+id);
	}
		
}
