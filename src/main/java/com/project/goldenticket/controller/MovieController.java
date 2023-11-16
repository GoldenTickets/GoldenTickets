package com.project.goldenticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.project.goldenticket.DTO.Movie;
import com.project.goldenticket.mapper.MovieMapper;

@RestController
@RequestMapping("/movie")
public class MovieController {
	
	@Autowired
	private MovieMapper movieMapper;
	
	@GetMapping("/{movie_id}")
	public ModelAndView getById(@PathVariable("movie_id") int movie_id) {
		ModelAndView mav = new ModelAndView("movieInfo");
		Movie movie = movieMapper.getById(movie_id);
		mav.addObject("movie", movie);
		return mav;
	}

}
