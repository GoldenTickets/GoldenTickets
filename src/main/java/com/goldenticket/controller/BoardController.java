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
import com.goldenticket.mapper.ArticleMapper;
import com.goldenticket.mapper.ReplyMapper;

@RestController
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private ArticleMapper articleMapper;
	
	@Autowired
	private ReplyMapper replyMapper;
	
	@GetMapping("")
	public ModelAndView getAll(@RequestParam(defaultValue = "1") int page) { // page = 현재페이지, pageSize도 나중에 정할 수 있게 바꾸기
		
		ModelAndView mav = new ModelAndView("articleList");
		int pageSize = 10;
		int startRow = (page-1)*pageSize;
		RowBounds rowBounds = new RowBounds(startRow, pageSize); // 페이징 처리
		List<Article> articles = articleMapper.getAll(rowBounds);
		mav.addObject("articles", articles);
		
		int totalArticles = articleMapper.totalArticles();
		int totalPages = (int) Math.ceil((double) totalArticles / pageSize); // 전체 페이지 수 구하기
		mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);
		
		return mav;
	}
	
	@GetMapping("/{id}")
	public ModelAndView getById(@PathVariable("id") int id) {
		ModelAndView mav = new ModelAndView("article");
		Article article = articleMapper.getById(id);
		List<Reply> replies = replyMapper.getByArticle_id(id);
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
		int cid = article.getCategory_id();
		articleMapper.save(article);
		article = articleMapper.getNewId();
		article.setCategory_id(cid);
		articleMapper.save2(article);
		return article;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@PutMapping("")
	public Article update(@RequestBody Article article) {
		articleMapper.articleUpdate(article);
		articleMapper.acUpdate(article); // 조건문 넣기
		return article;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@DeleteMapping("")
	public Article delete(@RequestBody Article article) {
		articleMapper.deleteac(article);
		articleMapper.deleteArticle(article);
		return article;
	}
}
