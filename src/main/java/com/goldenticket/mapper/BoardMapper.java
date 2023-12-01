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
	Article getById(@Param("id") int id) throws Exception;
	
	@Select("SELECT A.*, C.categoryname, M.nickname, COUNT(R.id) AS reply_count FROM article A INNER JOIN article_category AC on A.id = AC. article_id INNER JOIN category C ON AC. category_id = C.id INNER JOIN member M ON A.mem_id = M.id LEFT JOIN reply R ON A.id = R.article_id GROUP BY A.id, C.categoryname, M.nickname ORDER BY A.regdate DESC")
	List<Article> getAll(RowBounds rowBounds) throws Exception;
	
	@Select("SELECT A.*, C.categoryname, M.nickname, COUNT(R.id) AS reply_count FROM article A INNER JOIN article_category AC on A.id = AC. article_id INNER JOIN category C ON AC. category_id = C.id INNER JOIN member M ON A.mem_id = M.id LEFT JOIN reply R ON A.id = R.article_id WHERE C.id = #{category} GROUP BY A.id, C.categoryname, M.nickname ORDER BY regdate DESC")
	List<Article> getAllByCategory(RowBounds rowBounds, int category) throws Exception;
	
	@Select("SELECT R.*,M.nickname FROM reply R INNER JOIN member M ON R.mem_id = M.id WHERE R.article_id = #{id} ORDER BY R.regdate DESC")//댓글가져오기
	List<Reply> getByArticle_id(@Param("id") int id, RowBounds rowBounds) throws Exception;
	
	@Insert("INSERT INTO article (mem_id, title, content, regdate) VALUES (#{article.mem_id}, #{article.title}, #{article.content}, now())")
	int createArticle(@Param("article") Article article) throws Exception;
	
	@Insert("INSERT INTO article_category (article_id,category_id) VALUES (#{article_id}, #{category_id})")
	int createArticleCategory(int article_id,int category_id) throws Exception;
	
	@Select("SELECT MAX(id) AS id FROM article")
	Article getNewId() throws Exception;
	
	@Update("UPDATE article SET title = #{article.title}, content = #{article.content}, updatedate = now() WHERE id = #{article.id}")
	int articleUpdate(@Param("article") Article article) throws Exception;
	
	@Update("UPDATE article_category SET category_id = #{article.category_id} WHERE article_id = #{article.id}")
	int acUpdate(@Param("article") Article article) throws Exception;
	
	@Delete("DELETE FROM article WHERE id = #{id}")
	int deleteArticle(int id) throws Exception; // 파라미터 그냥 id로 바꾸기
	
	@Delete("DELETE FROM reply WHERE id = #{id}") //댓글삭제기능
	int deleteReply(int id) throws Exception;
	
	@Select("SELECT mem_id FROM reply WHERE id = #{reply_id}")//댓글 삭제 위해서 해당 댓글의 작성자가 session의 사용자와 일치하는지확인
	int confirmIdOfReply(int id) throws Exception;
	
	// 페이징 처리
	@Select("SELECT count(*) FROM article")
	int totalArticles() throws Exception;
	
	@Select("SELECT count(*) FROM article A JOIN article_category AC ON A.id = AC.article_id WHERE AC.category_id = #{category}")
	int totalArticlesByCategory(int category) throws Exception;
	
	@Select("SELECT COUNT(*) FROM reply WHERE article_id = #{id}")
	int getTotalreply(int id) throws Exception;
	
	@Insert("INSERT INTO reply (article_id,mem_id,content,regdate) VALUES (#{article_id},#{mem_id},#{content},current_timestamp)")
	int createReply(Reply reply) throws Exception;
	
	@Update("UPDATE article SET hit=#{hit} WHERE id=#{id}")
	int updateReplyHit(Article article) throws Exception;
	
	@Select("SELECT C.categoryname, A.*, M.nickname, COUNT(R.id) AS reply_count FROM article A JOIN article_category AC ON A.id = AC.article_id JOIN category C ON C.id = AC.category_id JOIN member M ON A.mem_id = M.id LEFT JOIN reply R ON A.id = R.article_id WHERE A.title LIKE CONCAT('%', #{keyword}, '%') GROUP BY A.id, C.categoryname, M.nickname ORDER BY regdate DESC")
	List<Article> getByTitle(RowBounds rowBounds, String keyword) throws Exception;
	
	@Select("SELECT C.categoryname, A.*, M.nickname, COUNT(R.id) AS reply_count FROM article A JOIN article_category AC ON A.id = AC.article_id JOIN category C ON C.id = AC.category_id JOIN member M ON A.mem_id = M.id LEFT JOIN reply R ON A.id = R.article_id WHERE M.nickname LIKE CONCAT('%', #{keyword}, '%') GROUP BY A.id, C.categoryname, M.nickname ORDER BY regdate DESC")
	List<Article> getByNickname(RowBounds rowBounds, String keyword) throws Exception;
	
	@Select("SELECT C.categoryname, A.*, M.nickname, COUNT(R.id) AS reply_count FROM article A JOIN article_category AC ON A.id = AC.article_id JOIN category C ON C.id = AC.category_id JOIN member M ON A.mem_id = M.id LEFT JOIN reply R ON A.id = R.article_id WHERE M.nickname LIKE CONCAT('%', #{keyword}, '%') OR A.title LIKE CONCAT('%', #{keyword}, '%') GROUP BY A.id, C.categoryname, M.nickname ORDER BY regdate DESC")
	List<Article> getByTitleAndNickname(RowBounds rowBounds, String keyword) throws Exception;
	
	@Select("SELECT COUNT(*) FROM article A JOIN article_category AC ON A.id = AC.article_id JOIN category C ON C.id = AC.category_id JOIN member M ON A.mem_id = M.id WHERE A.title LIKE CONCAT('%', #{keyword}, '%')")
	int totalArticlesByTitle(String keyword) throws Exception;
	
	@Select("SELECT COUNT(*) FROM article A JOIN article_category AC ON A.id = AC.article_id JOIN category C ON C.id = AC.category_id JOIN member M ON A.mem_id = M.id WHERE M.nickname LIKE CONCAT('%', #{keyword}, '%')")
	int totalArticlesByNickname(String keyword) throws Exception;
	
	@Select("SELECT COUNT(*) FROM article A JOIN article_category AC ON A.id = AC.article_id JOIN category C ON C.id = AC.category_id JOIN member M ON A.mem_id = M.id WHERE M.nickname LIKE CONCAT('%', #{keyword}, '%') OR A.title LIKE CONCAT('%', #{keyword}, '%')")
	int totalArticlesByTitleAndNickname(String keyword) throws Exception;
}

