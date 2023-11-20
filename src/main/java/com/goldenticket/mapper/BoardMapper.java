package com.goldenticket.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

import com.goldenticket.DTO.Article;
import com.goldenticket.DTO.Reply;

@Mapper
public interface BoardMapper {
	@Select("SELECT A.*, C.categoryname, M.nickname FROM article A INNER JOIN article_category AC on A.id = AC. article_id INNER JOIN category C ON AC. category_id = C.id INNER JOIN member M ON A.mem_id = M.id WHERE A.id = #{id}")
	Article getById(@Param("id") int id);
	
	@Select("SELECT A.*, C.categoryname, M.nickname FROM article A INNER JOIN article_category AC on A.id = AC. article_id INNER JOIN category C ON AC. category_id = C.id INNER JOIN member M ON A.mem_id = M.id")
	List<Article> getAll(RowBounds rowBounds);
	
	@Insert("INSERT INTO article (mem_id, title, content, hit, regdate, updatedate, likes, dislikes) VALUES (#{article.mem_id}, #{article.title}, #{article.content}, #{article.hit}, curdate(), curdate(), #{article.likes}, #{article.dislikes})")
	int save(@Param("article") Article article);
	
	@Select("SELECT MAX(id) AS id FROM article")
	Article getNewId();
	
	@Insert("INSERT INTO article_category (article_id, category_id) VALUES (#{article.id}, #{article.category_id})")
	int save2(@Param("article") Article article);
	
	@Update("UPDATE article SET title = #{article.title}, content = #{article.content}, updatedate = curdate() WHERE id = #{article.id}")
	int articleUpdate(@Param("article") Article article);
	
	@Update("UPDATE article_category SET category_id = #{article.category_id} WHERE article_id = #{article.id}")
	int acUpdate(@Param("article") Article article);
	
	@Delete("DELETE FROM article WHERE id = #{id}")
	int deleteArticle(int id); // 파라미터 그냥 id로 바꾸기
	
	@Delete("DELETE FROM article_category WHERE article_id = #{article.id}") // cascade 옵션 써서 이 메서드 지워버리기
	int deleteac(@Param("article") Article article);
	
	// 페이징 처리
	@Select("SELECT count(*) FROM article")
	int totalArticles();
	
	@Select("SELECT R.*,M.nickname FROM reply R INNER JOIN member M ON R.mem_id = M.id WHERE R.article_id = #{id}")
	List<Reply> getByArticle_id(@Param("id") int id);
	
	@Insert("INSERT INTO reply (article_id,mem_id,content,regdate) VALUES (#{article_id},#{mem_id},#{content},current_timestamp)")
	int createReply(Reply reply);
	
	@Update("UPDATE article SET hit=#{hit} WHERE id=#{id}")
	int updateReplyHit(Article article);
}

