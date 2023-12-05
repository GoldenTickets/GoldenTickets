package com.goldenticket.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goldenticket.DTO.Movie;
import com.goldenticket.DTO.Platform;
import com.goldenticket.DTO.Review;
import com.goldenticket.mapper.MovieMapper;

@Service
public class MovieService {
	
	@Autowired
	private MovieMapper movieMapper;
	
	public Movie getMovieById(int id) throws Exception {//영화 정보 보기
		return movieMapper.getMovieById(id);
	};
	
	public List<String> getMovieActors(int id) throws Exception {//영화 배우 목록 가져오기
		return movieMapper.getMovieActors(id);
	}
	
	public int updateHit(int id) throws Exception {
		Movie movie=movieMapper.getMovieById(id);
		movie.setHit(movie.getHit()+1);//조회수 1 증가
		return movieMapper.updateHit(movie);
	}
	
	//메서드 오버로딩
	public List<Movie> getAllMovies(RowBounds rowBounds,String order) throws Exception {
		if (order.equals("hit")) {
			return movieMapper.getAllMoviesByHit(rowBounds,order);
		}else {
			return movieMapper.getAllMoviesByReleasedate(rowBounds,order);
		}
	}
	//메서드 오버로딩
	public List<Movie> getAllMovies(RowBounds rowBounds,String order,int genre) throws Exception {
		if (order.equals("hit")) {
			return movieMapper.getAllMoviesByGenreByHit(rowBounds,order,genre);
		}else {
			return movieMapper.getAllMoviesByGenreByReleasedate(rowBounds,order,genre);
		}
	}
	
	//영화 사진 가져오기
	public List<Object> getMoviePhoto(int id) throws Exception { 
		//만약 사진이 5장이면 1장,4장으로 분리해야됨. carousel이 첫번째 사진을 class를 active로 따로 설정해야되기때문
		List<String> moviePhotoList=movieMapper.getMoviePhoto(id);
		String moviePhotoFirst= moviePhotoList.get(0);//carousel에 첫번째로 노출될 사진
		
		List<String> moviePhotoRemaining=moviePhotoList.subList(1,moviePhotoList.size());//2번째사진부터 마지막사진까지
		
		List<Object> list=new ArrayList<Object>();//List에 서로 다른 타입의 요소를 넣을수있게 제네릭 Object로 설정
		list.add(moviePhotoFirst);//list 객체의 첫번째 요소는 String형
		list.add(moviePhotoRemaining);//list 객체의 두번째 요소는 List<String>형
		return list;
	}
	
	//영화 장르 가져오기
	public List<String> getMovieGenre(int id) throws Exception {
		return movieMapper.getMovieGenre(id);
	}
	
	//영화 플랫폼 가져오기
	public List<Platform> getMoviePlatform(int id) throws Exception {
		return movieMapper.getMoviePlatform(id);
	}
	
	//영화 리뷰 가져오기
	public List<Review> getMovieReview(int id, RowBounds rowBounds) throws Exception {
		return movieMapper.getMovieReview(id, rowBounds);
	}
	
	//영화 리뷰 작성하기
	public int createMovieReview(Review review,int id,int mem_id) {
		try {
				int reviewCount = movieMapper.getReviewCount(id, mem_id);//리뷰 작성하기 위해 해당리뷰에 작성한 사용자의 리뷰갯수 가져옴
				if(reviewCount==1) {
					return 2;//해당영화에 대해 작성한 리뷰가 이미 있는경우 2를 반환
				}else {
					int result = movieMapper.createMovieReview(review,id);//해당영화에 대해 작성한 리뷰갯수가 0일경우,그리고 성공했을경우 1 반환. 실패하면 예외발생
					result = movieMapper.updateReviewRating(id);//새로 구한 평균으로 rating 컬럼값 업데이트
					return result;
				}
			}catch(Exception e) {
				e.printStackTrace();
				return 3;
			}
	}
	
	//리뷰 작성하기 위해 이미 작성한 리뷰가 몇개인지확인
	public int getReviewCount(int movie_id,int mem_id) throws Exception {
		return movieMapper.getReviewCount(movie_id, mem_id);
	}
	

	//리뷰 삭제기능.
	public int deleteReview(int movie_id,int mem_id) {
		try {
			int result = movieMapper.deleteReview(movie_id, mem_id);
			result=movieMapper.updateReviewRating(movie_id);
			return result;
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
		
	}
	
	//movieinfo 페이지에서 로그인한 회원에 대해 해당영화가 북마크되었는지 확인
	public int IsitBookdmarkedById(int movie_id,int mem_id) throws Exception {
		return movieMapper.IsitBookdmarkedById(movie_id, mem_id);//1이 반환되면 해당영화의 해당 아이디가 북마크 이미 한것. 0이면 안한것
	}
	
	//해당 아이디의 북마크한 모든 영화목록
	public List<Integer> bookmarkListById(int mem_id) throws Exception {
		return movieMapper.getBookmarkedMoviesById(mem_id);
	};
	
	//북마크 추가
	public int createBookmark(int movie_id,int mem_id) throws Exception {
		return movieMapper.createBookmark(movie_id, mem_id);
	}
	
	//북마크 삭제
	public int deleteBookmark(int movie_id,int mem_id) throws Exception {
		return movieMapper.deleteBookmark(movie_id, mem_id);
	}
	
	//모든 영화 갯수 가져오기
	public int totalMovies() throws Exception{
		return movieMapper.totalMovies();
	}
	
	//장르로 분류한 영화 갯수 가져오기
	public int totalMoviesByGenre(int genre) throws Exception{
		return movieMapper.totalMoviesByGenre(genre);
	}
	
	//리뷰 갯수 가져오기
	public int getTotalreviews(int id) throws Exception {
		return movieMapper.getTotalreviews(id);
	}
	
	//영화 평점순으로 정렬해서 가져오기
	public List<Movie> getRanking(RowBounds rowBounds) throws Exception{
		return movieMapper.getRanking(rowBounds);
	}
	
	////영화 평점순으로 정렬후 장르별로 가져오기
	public List<Movie> getRankingByGenre(RowBounds rowBounds, int genre) throws Exception{
		return movieMapper.getRankingByGenre(rowBounds, genre);
	}
	
	//영화 검색
	public List<Movie> getBySearch(RowBounds rowBounds, String subject, List<String> genre, String keyword){
		return movieMapper.getBySearch(rowBounds, subject, genre, keyword);
	}
	
	//검색 결과 레코드 수
	public int totalmoviesBySearch(String subject, List<String> genre, String keyword) {
		return movieMapper.totalmoviesBySearch(subject, genre, keyword);
	}
}
