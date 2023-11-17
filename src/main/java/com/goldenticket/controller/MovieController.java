package com.goldenticket.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.goldenticket.DTO.Movie;
import com.goldenticket.DTO.Review;
import com.goldenticket.service.MovieService;

@RestController
@RequestMapping("/movieinfo")
public class MovieController {

	@Autowired
	private MovieService movieService;
	
	@GetMapping("/{id}")
	public ModelAndView getAllMovies(@PathVariable int id)throws Exception {
		ModelAndView mav=new ModelAndView("movieinfo");

		Movie movie = movieService.getMovieById(id);
		List<Object> moviePhotoList = movieService.getMoviePhoto(id);
		mav.addObject("movie", movie);
		mav.addObject("PhotoFirst", moviePhotoList.get(0));//첫번째 사진
		mav.addObject("PhotoRemaining", moviePhotoList.get(1));//첫번째 사진 제외한 나머지 사진
		mav.addObject("movieGenre",movieService.getMovieGenre(id));//영화 장르가져오기 (List)
		mav.addObject("moviePlatform", movieService.getMoviePlatform(id));//영화 플랫폼가져오기
		mav.addObject("movieReviewList", movieService.getMovieReview(id));//영화 리뷰가져오기
		return mav;
		}
	
	@PostMapping("/submitreview/{id}")
	public ModelAndView reviewSubmit(@RequestBody Review review,@PathVariable int id) {
		System.out.println(review);
		movieService.createMovieReview(review,id);
		ModelAndView mav=new ModelAndView("redirect:/movieinfo/"+id);
		return mav;
	}
	
}
