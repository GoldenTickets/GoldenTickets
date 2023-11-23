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
	
	//게시글 상세
	public Article getArticleById(int id){
		Article article=boardMapper.getById(id);
		return article;
	}
	
	
	//게시글 조회수증가
	public int updateHit(int id){
		Article article=boardMapper.getById(id);
		article.setHit(article.getHit()+1);//조회수 1 증가
		return boardMapper.updateReplyHit(article);
	}
	
	//게시글 글쓰기
	public int createArticle(Article article) {
		boardMapper.createArticle(article);
		boardMapper.createArticleCategory(boardMapper.getNewId().getId(),article.getCategory_id());
		return 1;
	}
	
	
	//리뷰작성하기
	public int createReply(Reply reply){
		return boardMapper.createReply(reply);
	};
	
	//글수정하기
	public int updateArticle(Article article){
		boardMapper.acUpdate(article);
		return boardMapper.articleUpdate(article);
	}
	
	//글삭제하기
	public int deleteArticle(int id) throws Exception{
		return boardMapper.deleteArticle(id);
	}
	
	//댓글삭제하기
	public int deleteReply(int id) throws Exception{
		return boardMapper.deleteReply(id);
	}
	
	//댓글 수정,삭제하기 위해 아이디일치확인
	public int confirmIdOfReply(int id) throws Exception{
		return boardMapper.confirmIdOfReply(id);
	}
}
