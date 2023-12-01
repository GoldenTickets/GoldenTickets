package com.goldenticket.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
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
	public Article getArticleById(int id) throws Exception {
		Article article=boardMapper.getById(id);
		return article;
	}
	
	//검색 키워드(제목)에 따른 게시물목록 
	public List<Article> getByTitle(RowBounds rowBounds, String keyword) throws Exception {
		return boardMapper.getByTitle(rowBounds, keyword);
	}
	
	//총 게시물 갯수
	public int totalArticlesByTitle(String keyword) throws Exception {
		return boardMapper.totalArticlesByTitle(keyword);
	}
	
	//검색 키워드(닉네임)에 따른 게시물목록
	public List<Article> getByNickname(RowBounds rowBounds,String keyword) throws Exception {
		return boardMapper.getByNickname(rowBounds, keyword);
	}
	
	//검색 키워드(닉네임)에 따른 모든 게시물 갯수
	public int totalArticlesByNickname(String keyword) throws Exception  {
		return boardMapper.totalArticlesByNickname(keyword);
	}
	
	//검색 키워드(제목과 닉네임)에 따른 모든 게시물
	public List<Article> getByTitleAndNickname(RowBounds rowBounds,String keyword) throws Exception  {
		return boardMapper.getByTitleAndNickname(rowBounds, keyword);
	}
	
	//검색 키워드(제목과 닉네임)에 따른 모든 게시물 갯수
	public int totalArticlesByTitleAndNickname(String keyword) throws Exception {
		return boardMapper.totalArticlesByTitleAndNickname(keyword);
	}
	
	//페이지당 보여지는 게시물 수에 따른 모든 게시물
	public List<Article> getAll(RowBounds rowBounds) throws Exception{
		return boardMapper.getAll(rowBounds);
	}
	
	//모든게시물 가져오기
	public int totalArticles() throws Exception{
		return boardMapper.totalArticles();
	}
	
	//카테고리에 따른 게시글 목록
	public List<Article>getAllByCategory(RowBounds rowBounds, int category) throws Exception {
		return boardMapper.getAllByCategory(rowBounds, category);
	}
	
	//카테고리에 따른 게시글 갯수
	public int totalArticlesByCategory(int category) throws Exception {
		return boardMapper.totalArticlesByCategory(category);
	}
	
	//단일 게시글 보기
	public Article getById(int id) throws Exception{
		return boardMapper.getById(id);
	}
	
	//단일 게시글의 댓글 목록
	public List<Reply> getByArticle_id(int id, RowBounds rowBounds)throws Exception{
		return boardMapper.getByArticle_id(id, rowBounds);
	}
	
	//전체 댓글갯수 
	public int getTotalreply(int id) throws Exception {
		return boardMapper.getTotalreply(id);
	}
	
	//게시글 조회수증가
	public int updateHit(int id) throws Exception{
		Article article=boardMapper.getById(id);
		article.setHit(article.getHit()+1);//조회수 1 증가
		return boardMapper.updateReplyHit(article);
	}
	
	//게시글 글쓰기
	public int createArticle(Article article) throws Exception {
		boardMapper.createArticle(article);
		boardMapper.createArticleCategory(boardMapper.getNewId().getId(),article.getCategory_id());
		return 1;
	}
	
	
	//리뷰작성하기
	public int createReply(Reply reply) throws Exception {
		return boardMapper.createReply(reply);
	};
	
	//글수정하기
	public int updateArticle(Article article) throws Exception {
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
