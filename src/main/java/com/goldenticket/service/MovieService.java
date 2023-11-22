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
	
	public int createMovieReview(Review review,int id) {//영화 리뷰 작성하기
		return movieMapper.createMovieReview(review,id);
	}
}
