package com.goldenticket.controller;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.goldenticket.DTO.Movie;
import com.goldenticket.DTO.Review;
import com.goldenticket.mapper.MovieMapper;
import com.goldenticket.service.MovieService;

@ControllerAdvice
@RestController
@RequestMapping("/movie")
public class MovieController {

	@Autowired
	private MovieMapper movieMapper;
	
	@Autowired
	private MovieService movieService;
	

	@GetMapping("/{id}")
	public ModelAndView getAllMovies(@RequestParam(defaultValue = "1") int page, @PathVariable int id)throws Exception {
		movieService.updateHit(id); //조회수 1 증가
		ModelAndView mav=new ModelAndView("movieinfo");
		
		int pageSize = 10;
		int startRow = (page-1)*pageSize;
		RowBounds rowBounds = new RowBounds(startRow, pageSize); // 페이징 처리
		
		int totalArticles = movieMapper.getTotalreviews(id);
		int totalPages = (int) Math.ceil((double) totalArticles / pageSize); // 전체 페이지 수 구하기
		mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);
		
		Movie movie = movieService.getMovieById(id);
		List<Object> moviePhotoList = movieService.getMoviePhoto(id);
		mav.addObject("movie", movie);
		mav.addObject("PhotoFirst", moviePhotoList.get(0));//첫번째 사진
		mav.addObject("PhotoRemaining", moviePhotoList.get(1));//첫번째 사진 제외한 나머지 사진
		mav.addObject("movieGenre",movieService.getMovieGenre(id));//영화 장르가져오기 (List)
		mav.addObject("moviePlatform", movieService.getMoviePlatform(id));//영화 플랫폼가져오기
		mav.addObject("movieReviewList", movieService.getMovieReview(id, rowBounds));//영화 리뷰가져오기
		return mav;
		}
	

	@PostMapping("/submitreview/{id}")
	public int reviewSubmit(@RequestBody Review review,@PathVariable int id) {
		int result = movieService.createMovieReview(review,id);
		
		return result;
	}
	

	@GetMapping("")
	public ModelAndView getAll(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "0") int genre, @RequestParam(defaultValue = "update") String order) { // page = 현재페이지, pageSize도 나중에 정할 수 있게 바꾸기
		
		ModelAndView mav = new ModelAndView("movieInfo_all");
		int pageSize = 15;
		int startRow = (page-1)*pageSize;
		RowBounds rowBounds = new RowBounds(startRow, pageSize); // 페이징 처리
		List<Movie> movies;
		
		if (genre == 0) {
			movies = movieMapper.getAllMovies(rowBounds, order);
		}else {
			movies = movieMapper.getAllMoviesByGenre(rowBounds, order, genre);
		}
		mav.addObject("order", order);
		mav.addObject("genre", genre);
		
		int totalMovies = movieMapper.totalMovies();
		int totalPages = (int) Math.ceil((double) totalMovies / pageSize); // 전체 페이지 수 구하기
		
		mav.addObject("movies", movies);
		mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);
		
		return mav;
	}
	

	@GetMapping("/ranking")
		public ModelAndView getRanking(@RequestParam(defaultValue = "1") int page) { // page = 현재페이지, pageSize도 나중에 정할 수 있게 바꾸기
		
		ModelAndView mav = new ModelAndView("movieRanking");
		int pageSize = 10;
		int startRow = (page-1)*pageSize;
		RowBounds rowBounds = new RowBounds(startRow, pageSize); // 페이징 처리
		List<Movie> movies = movieMapper.getRanking(rowBounds);
		mav.addObject("movies", movies);
		
		int totalMovies = movieMapper.totalMovies();
		int totalPages = (int) Math.ceil((double) totalMovies / pageSize); // 전체 페이지 수 구하기
		mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);
		
		return mav;
	}
	
}
