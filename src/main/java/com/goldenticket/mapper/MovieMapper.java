package com.goldenticket.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

import com.goldenticket.DTO.Movie;
import com.goldenticket.DTO.Platform;
import com.goldenticket.DTO.Review;

@Mapper
public interface MovieMapper {
	
	//DB 영화 가져오기(영화 평점은 해당영화의 리뷰 테이블의 평균을 조인으로 가져와서 avg_rating 별칭사용한 컬럼으로 가져옴)
	//@Select("SELECT M.*, format(AVG(R.RATING),1) AS avg_rating FROM movie M LEFT JOIN review R ON M.id = R.movie_id WHERE M.id = #{id} GROUP BY M.id")
	@Select("SELECT * FROM movie WHERE id = #{id}")
	Movie getMovieById(@Param("id")int id);
	
	@Update("UPDATE movie SET hit=#{hit} WHERE id=#{id}")
	int updateHit(Movie movie);
	
	//영화의 사진 모두 가져오기 (poster아님)
	@Select("SELECT photoname FROM movie_photo WHERE movie_id = #{movie_id}")
	List<String> getMoviePhoto(@Param("movie_id") int movie_id);
	
	//영화 장르 모두 가져오기
	@Select("SELECT G.name FROM movie_genre MG LEFT OUTER JOIN genre G ON MG.genre_id=G.id WHERE MG.movie_id=#{id}")
	List<String> getMovieGenre(int id);
	
	@Select("SELECT A.name FROM movie_actor MA INNER JOIN actor A on MA.actor_id=A.id WHERE MA.movie_id = #{movie_id}")
	List<String> getMovieActors(int id);
	
	//영화 볼수있는곳 플랫폼의 이름,url 모두 가져오기
	@Select("SELECT P.name,P.url FROM movie_platform MP INNER JOIN platform P ON MP.platform_id=P.id WHERE MP.movie_id=#{id}")
	List<Platform> getMoviePlatform(int id);
	
	//영화 리뷰 가져오기
	@Select("SELECT R.*,M.nickname FROM review R INNER JOIN member M ON R.mem_id=M.id WHERE R.movie_id=#{id} ORDER BY R.regdate DESC")
	List<Review> getMovieReview(int id, RowBounds rowBounds);
	
	//총 리뷰수 구하기
	@Select("SELECT COUNT(*) FROM review WHERE movie_id = #{id}")
	int getTotalreviews(int id);
	
	//영화 리뷰 쓰기
	@Insert("INSERT INTO review (movie_id,mem_id,rating,content,regdate) values(#{review.movie_id},#{review.mem_id},#{review.rating},#{review.content},current_timestamp)")
	int createMovieReview(Review review,int id);
	
	//모든 영화 가져오기 (조회수순)  //ORDER BY {order}이렇게 하고싶었으나 이런방식으로 order by가 적용이안되는관계로 hit에 따른 메서드를 따로 만듬
	@Select("SELECT id, title, poster, rating FROM movie ORDER BY hit DESC")
	List<Movie> getAllMoviesByHit(RowBounds rowBounds, @Param("order") String order);
	
	//모든 영화 가져오기 (개봉일순)  //ORDER BY {order}이렇게 하고싶었으나 이런방식으로 order by가 적용이안되는관계로 releasedate에 따른 메서드를 따로 만듬
	@Select("SELECT id, title, poster, rating FROM movie ORDER BY releasedate DESC")
	List<Movie> getAllMoviesByReleasedate(RowBounds rowBounds, @Param("order") String order);
	
	//장르별 영화 가져오기(조회수순) //ORDER BY {order}이렇게 하고싶었으나 이런방식으로 order by가 적용이안되는관계로 hit에 따른 메서드를 따로 만듬
	@Select("SELECT M.id, M.title, M.poster, M.rating FROM movie M JOIN movie_genre MG ON M.id = MG.movie_id JOIN genre G ON G.id = MG.genre_id WHERE G.id = #{genre} ORDER BY hit DESC")
	List<Movie> getAllMoviesByGenreByHit(RowBounds rowBounds, String order, int genre);
	
	//장르별 영화 가져오기(조회수순) (개봉일순)  //ORDER BY {order}이렇게 하고싶었으나 이런방식으로 order by가 적용이안되는관계로 releasedate에 따른 메서드를 따로 만듬
	@Select("SELECT M.id, M.title, M.poster, M.rating FROM movie M JOIN movie_genre MG ON M.id = MG.movie_id JOIN genre G ON G.id = MG.genre_id WHERE G.id = #{genre} ORDER BY releasedate DESC")
	List<Movie> getAllMoviesByGenreByReleasedate(RowBounds rowBounds, String order, int genre);
	
	//모든 영화 갯수 가져오기
	@Select("SELECT count(*) FROM movie")
	int totalMovies();
	
	//장르별 영화 갯수 가져오기
	@Select("SELECT count(*) FROM movie M JOIN movie_genre MG ON M.id = MG.movie_id WHERE MG.genre_id = #{genre}")
	int totalMoviesByGenre(int genre);
	
	//모든 영화 평점순으로 가져오기
	@Select("SELECT id, title, synopsis, releasedate, runningtime, poster, rating, ROW_NUMBER() OVER (ORDER BY rating DESC) as ranking FROM movie ORDER BY rating DESC")
	List<Movie> getRanking(RowBounds rowBounds);
	
	//장르별 영화 평점순으로 가져오기
	@Select("SELECT M.id, M.title, M.synopsis, M.releasedate, M.runningtime, M.poster, M.rating, ROW_NUMBER() OVER (ORDER BY M.rating DESC) as ranking FROM movie M JOIN movie_genre MG ON M.id = MG.movie_id JOIN genre G ON G.id = MG.genre_id WHERE G.id = #{genre} ORDER BY M.rating DESC")
	List<Movie> getRankingByGenre(RowBounds rowBounds, int genre);
	
	//신작영화 번호 리스트 가져오기 -> 관리자 관리
	@Select("SELECT movie_id FROM new_movie")
	List<Integer> getNewmovie_ids();
	
	//신작영화 id, title, rating, 사진이름 가져오기
	@Select("SELECT DISTINCT M.id, M.title, M.rating, MP.photoname FROM movie M JOIN movie_photo MP ON M.id = MP.movie_id WHERE M.id = #{id} ORDER BY RAND() LIMIT 1")
	Movie getNewmovie(int id);
	
	//신작영화 장르가져오기
	@Select("SELECT G.name FROM genre G JOIN movie_genre MG ON G.id = MG.genre_id WHERE MG.movie_id = #{id}")
	List<String> getNewmovieGenres(int id);
	
	//신작영화 국가명 가져오기
	@Select("SELECT C.name FROM country C JOIN movie_country MC ON C.id = MC.country_id WHERE MC.movie_id = #{id}")
	List<String> getNewmovieCountrys(int id);
	
	//오늘의 추천 영화 5개 가져오기
	@Select("SELECT M.id, M.title, M.poster, M.rating FROM movie M JOIN today_movie TM ON M.id = TM.movie_id WHERE TM.id BETWEEN 1 AND 5")
	List<Movie> getTodaymovies();
	
	//새로운 오늘의 추천 영화 id 5개 가져오기
	@Select("SELECT DISTINCT id FROM movie WHERE rating >= 8.5 AND id NOT IN (select movie_id FROM today_movie) ORDER BY RAND() LIMIT 5")
	List<Integer> getNewtodaymovie();
	
	//오늘의 추천 영화 테이블 비우기
	@Delete("DELETE FROM today_movie WHERE id BETWEEN 1 AND 5")
	int deletetodaymovies();
	
	//새로운 오늘의 추천 영화 갱신하기
	@Insert("INSERT INTO today_movie (id, movie_id) VALUES (#{id}, #{movie_id}) ON DUPLICATE KEY UPDATE id=id")
	int saveNewtodaymovie(int id, int movie_id);
	
	//회원님을 위한 맞춤 영화 10개 가져오기
	@Select("SELECT DISTINCT M.id, M.title, M.poster, M.rating FROM movie M JOIN movie_genre MG ON MG.movie_id = M.id WHERE MG.genre_id IN (SELECT genre_id FROM member_genre WHERE mem_id = 1) ORDER BY RAND() LIMIT 10")
	List<Movie> getCustommovies(int id);
	
	//골든티켓 회원들이 사랑하는 영화 10개
	@Select("SELECT id, title, poster, rating FROM movie ORDER BY hit DESC LIMIT 10")
	List<Movie> getLovedmovies();
	
	//상위 10개 영화 가져오기
	@Select("SELECT id, title, poster, ROW_NUMBER() OVER (ORDER BY rating DESC) as ranking FROM movie ORDER BY rating DESC LIMIT 10;")
	List<Movie> getTopMovies();
	
	//리뷰 이미 작성했는지 확인하기 위해 해당영화에 대한 회원의 리뷰 갯수확인
	@Select("SELECT count(*) FROM review WHERE movie_id=#{movie_id} and mem_id=#{mem_id}")
	int getReviewCount(int movie_id,int mem_id);
	
	//리뷰평점 가져오기
	//@Select("SELECT AVG(rating) FROM review GROUP BY movie_id HAVING movie_id = #{movie_id}")
	//double getReviewRating(int movie_id);
	
	//리뷰 작성 후,리뷰 삭제 후 해당영화 평균 평점 업데이트
	//@Update("UPDATE movie SET rating = #{newRating} WHERE id = #{movie_id}") 서브쿼리사용으로 변경
	@Update("UPDATE movie SET rating = (SELECT AVG(rating) FROM review GROUP BY movie_id HAVING movie_id = #{movie_id})  WHERE id = #{movie_id}")
	int updateReviewRating(int movie_id);
	
	//리뷰 삭제기능
	@Delete("DELETE FROM review WHERE movie_id = #{movie_id} AND mem_id = #{mem_id}")
	int deleteReview(int movie_id,int mem_id);

	//회원아이디의 아이디에 해당영화가 북마크 되었는지 확인 
	@Select("SELECT COUNT(*) FROM bookmark WHERE movie_id = #{movie_id} AND mem_id = #{mem_id}")
	int IsitBookdmarkedById(int movie_id,int mem_id);
	
	//회원아이디에 따른 북마크된 영화 목록
	@Select("SELECT movie_id FROM bookmark WHERE mem_id = #{mem_id}")
	List<Integer> getBookmarkedMoviesById(int mem_id);
	
	//북마크 추가
	@Insert("INSERT INTO bookmark(movie_id,mem_id) VALUES (#{movie_id},#{mem_id})")
	int createBookmark(int movie_id,int mem_id);
	
	//북마크 삭제
	@Delete("DELETE FROM bookmark WHERE movie_id = #{movie_id} AND mem_id = #{mem_id}")
	int deleteBookmark(int movie_id,int mem_id);

}
