package com.goldenticket.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goldenticket.DTO.Article;
import com.goldenticket.DTO.Movie;
import com.goldenticket.DTO.Platform;
import com.goldenticket.DTO.Review;
import com.goldenticket.mapper.MovieMapper;

@Service
public class MovieService {
	
	@Autowired
	private MovieMapper movieMapper;
	
	public Movie getMovieById(int id){//영화 정보 보기
		return movieMapper.getMovieById(id);
	};
	

	public int updateHit(int id) {
		Movie movie=movieMapper.getMovieById(id);
		movie.setHit(movie.getHit()+1);//조회수 1 증가
		return movieMapper.updateHit(movie);
	}
	
	public List<Object> getMoviePhoto(int id) {//영화 사진 가져오기 
		//만약 사진이 5장이면 1장,4장으로 분리해야됨. carousel이 첫번째 사진을 class를 active로 따로 설정해야되기때문
		List<String> moviePhotoList=movieMapper.getMoviePhoto(id);
		String moviePhotoFirst= moviePhotoList.get(0);//carousel에 첫번째로 노출될 사진 
		List<String> moviePhotoRemaining=moviePhotoList.subList(1,moviePhotoList.size());//2번째사진부터 마지막사진까지
		
		List<Object> list=new ArrayList<Object>();//List에 서로 다른 타입의 요소를 넣을수있게 제네릭 Object로 설정 
		list.add(moviePhotoFirst);//list 객체의 첫번째 요소는 String형
		list.add(moviePhotoRemaining);//list 객체의 두번째 요소는 List<String>형
		return list;
	}
	
	public List<String> getMovieGenre(int id){//영화 장르 가져오기
		return movieMapper.getMovieGenre(id);
	}
	
	public List<Platform> getMoviePlatform(int id){//영화 플랫폼 가져오기
		return movieMapper.getMoviePlatform(id);
	}
	
	public List<Review> getMovieReview(int id, RowBounds rowBounds){//영화 리뷰 가져오기
		return movieMapper.getMovieReview(id, rowBounds);
	}
	
	public int createMovieReview(Review review,int id,int mem_id){//영화 리뷰 작성하기
		int reviewCount = movieMapper.getReviewCount(id, mem_id);//리뷰 작성하기 위해 해당리뷰에 작성한 사용자의 리뷰갯수 가져옴
		if(reviewCount==1) {
			return 2;//해당영화에 대해 작성한 리뷰가 이미 있는경우 2를 반환
		}else {
			int result = movieMapper.createMovieReview(review,id);//해당영화에 대해 작성한 리뷰갯수가 0일경우,그리고 성공했을경우 1 반환. 실패하면 예외발생
				try {
				double newRating = movieMapper.getReviewRating(id);

				movieMapper.updateReviewRating(newRating,id);//새로 구한 평균으로 rating 컬럼값 업데이트
				
				return result;
				}catch(Exception e) {
					e.printStackTrace();
					return 3;
				}
		}
	}
	
	public int getReviewCount(int movie_id,int mem_id){//리뷰 작성하기 위해 이미 작성한 리뷰가 몇개인지확인
		return movieMapper.getReviewCount(movie_id, mem_id);
	}
	
	public int deleteReview(int movie_id,int mem_id) {//리뷰 삭제기능. 
		int result = movieMapper.deleteReview(movie_id, mem_id);
		double newRating = movieMapper.getReviewRating(movie_id);
		result=movieMapper.updateReviewRating(newRating,movie_id);
		return result;
	}
}
