package com.goldenticket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goldenticket.DTO.Article;
import com.goldenticket.DTO.Reply;
import com.goldenticket.mapper.BoardMapper;

@Service
public class BoardService {
	
	@Autowired
	private BoardMapper boardMapper;
	
	//게시글 조회수증가
	public int updateHit(int id) {
		Article article=boardMapper.getById(id);
		article.setHit(article.getHit()+1);//조회수 1 증가
		return boardMapper.updateReplyHit(article);
	}
	
	//리뷰작성하기
	public int createReply(Reply reply) {
		return boardMapper.createReply(reply);
	};
}
