package com.goldenticket.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import com.goldenticket.DTO.Movie;
import com.goldenticket.DTO.Platform;
import com.goldenticket.DTO.Review;

@Mapper
public interface MovieMapper {
	
	//DB 영화 모두 가져오기(영화 평점은 해당영화의 리뷰 테이블의 평균을 조인으로 가져와서 avg_rating 별칭사용한 컬럼으로 가져옴)
	@Select("SELECT M.*, format(AVG(R.RATING),1) AS avg_rating FROM movie M LEFT JOIN review R ON M.id = R.movie_id WHERE M.id = #{id} GROUP BY M.id")
	Movie getMovieById(@Param("id")int id);
	
	//영화의 사진 모두 가져오기 (poster아님)
	@Select("SELECT photoname FROM movie_photo WHERE movie_id = #{movie_id}")
	List<String> getMoviePhoto(@Param("movie_id") int movie_id);
	
	//영화 장르 모두 가져오기
	@Select("SELECT G.name FROM movie_genre MG LEFT OUTER JOIN genre G ON MG.genre_id=G.id WHERE MG.movie_id=#{id}")
	List<String> getMovieGenre(int id);
	
	//영화 볼수있는곳 플랫폼의 이름,url 모두 가져오기
	@Select("SELECT P.name,P.url FROM movie_platform MP INNER JOIN platform P ON MP.platform_id=P.id WHERE MP.movie_id=#{id}")
	List<Platform> getMoviePlatform(int id);
	
	//영화 리뷰 가져오기
	@Select("SELECT R.*,M.nickname FROM review R INNER JOIN member M ON R.mem_id=M.id WHERE R.movie_id=#{id} ORDER BY R.regdate DESC")
	List<Review> getMovieReview(int id);
	
	//영화 리뷰 쓰기
	@Insert("INSERT INTO review (movie_id,mem_id,rating,content,regdate) values(#{review.movie_id},#{review.mem_id},#{review.rating},#{review.content},current_timestamp)")
	int createMovieReview(Review review,int id);
	
	@Select("SELECT id, title, poster, rating FROM movie")
	List<Movie> getAll(RowBounds rowBounds);
	
	@Select("SELECT count(*) FROM movie")
	int totalMovies();
	
	@Select("SELECT id, title, synopsis, releasedate, director, country, runningtime, poster, trailer, rating, ROW_NUMBER() OVER (ORDER BY rating DESC) as ranking FROM movie ORDER BY rating DESC")
	List<Movie> getRanking(RowBounds rowBounds);
}
