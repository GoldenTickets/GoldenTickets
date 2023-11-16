package com.project.goldenticket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.project.goldenticket.DTO.Article;
import com.project.goldenticket.DTO.Reply;
import com.project.goldenticket.mapper.ArticleMapper;
import com.project.goldenticket.mapper.ReplyMapper;

@RestController
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private ArticleMapper articleMapper;
	
	@Autowired
	private ReplyMapper replyMapper;
	
	@GetMapping("")
	public ModelAndView getAll() {
		ModelAndView mav = new ModelAndView("articleList");
		List<Article> articles = articleMapper.getAll();
		mav.addObject("articles", articles);
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
}
