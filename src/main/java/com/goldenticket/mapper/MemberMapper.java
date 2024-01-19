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
import com.goldenticket.DTO.Member;
import com.goldenticket.DTO.Movie;
import com.goldenticket.DTO.Reply;
import com.goldenticket.DTO.Review;

@Mapper
public interface MemberMapper {
	
	//비밀번호가 일치하는지 확인
	@Select("SELECT password from member WHERE email = #{email}")
	String logincheck(String email) throws Exception;
	
	//닉네임, id 번호 가져오기
	@Select("SELECT id,nickname from member WHERE email = #{email}")
	Member getMember(String nickname) throws Exception;

	//회원가입
	@Insert("INSERT INTO member (email,password,nickname,name) VALUES (#{email},#{password},#{nickname},#{name})")
	int createMember(Member member) throws Exception;
	
	//회원가입 시 선호하는 장르 저장
	@Insert("INSERT INTO member_genre (genre_id,mem_id) VALUES (#{genre_id},#{id})")
	int setMemberGenre(@Param("genre_id")int genre_id,@Param("id")int id) throws Exception;
	
	//회원정보 전부 가져오기 
	@Select("SELECT * FROM member WHERE id = #{id}")
	Member getMemberinfo(int id) throws Exception;
	
	//회원정보 수정페이지에서 회원이 선호하는 장르 목록 가져오기
	@Select("SELECT genre_id FROM member_genre WHERE mem_id = #{mem_id}")
	List<Integer> getMemGenreList(int mem_id) throws Exception;
	
	//회원정보 수정하기
	@Update("UPDATE member set name=#{name},email=#{email},password=#{password},nickname=#{nickname} WHERE id=#{id}")
	int updateMember(Member member) throws Exception;
	
	//회원 선호 장르 모두 삭제 (수정하기 위해서 먼저 삭제)
	@Delete("DELETE FROM member_genre where mem_id=#{mem_id}")
	int deleteMember(int id) throws Exception;
	
	// 북마크된 영화 목록 가져오기
	@Select("SELECT M.id, M.title, M.poster, M.rating FROM movie M JOIN bookmark B ON M.id = B.movie_id WHERE B.mem_id = #{id}")
	List<Movie> getBookmark(RowBounds rowBounds, int id) throws Exception;
	
	// 북마크 영화 갯수 가져오기
	@Select("SELECT count(*) FROM bookmark WHERE mem_id = #{id}")
	int getTotalbookmark(int id) throws Exception;
	
	// 내가 쓴 리뷰 목록 가져오기
	@Select("SELECT R.movie_id, M.title, R.id , R.rating, R.content, R.regdate FROM review R JOIN movie M ON R.movie_id = M.id WHERE mem_id = #{id}")
	List<Review> getMyreviews(RowBounds rowBounds, int id) throws Exception;
	
	// 내가 쓴 리뷰의 갯수 가져오기
	@Select("SELECT count(*) FROM review WHERE mem_id = #{id}")
	int getMytotalreviews(int id) throws Exception;
	
	// 내가 쓴 글 가져오기
	@Select("SELECT C.categoryname, A.id, A.title, A.hit, A.regdate FROM article A JOIN article_category AC ON A.id = AC.article_id JOIN category C ON AC.category_id = C.id WHERE A.mem_id = #{id}")
	List<Article> getMyarticles(RowBounds rowBounds, int id) throws Exception;
		
	// 내가 쓴 글 갯수 가져오기
	@Select("SELECT count(*) FROM article WHERE mem_id = #{id}")
	int getMytotalarticles(int id) throws Exception;
	
	// 내가 쓴 댓글 가져오기
	@Select("SELECT R.article_id, A.title, R.id,R.content, R.regdate FROM reply R JOIN article A ON R.article_id = A.id WHERE R.mem_id = #{id}")
	List<Reply> getMyreplies(RowBounds rowBounds, int id) throws Exception;
		
	// 내가 쓴 댓글 갯수 가져오기
	@Select("SELECT count(*) FROM reply WHERE mem_id = #{id}")
	int getMytotalreplies(int id) throws Exception;
}
