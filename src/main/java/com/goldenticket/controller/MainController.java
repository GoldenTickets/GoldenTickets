package com.goldenticket.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.goldenticket.DTO.Movie;
import com.goldenticket.mapper.MovieMapper;

@RestController
@RequestMapping("/")
public class MainController {

	@Autowired
	private MovieMapper movieMapper;
	
	@GetMapping("")
	public ModelAndView getAll(HttpSession session){

			ModelAndView mav = new ModelAndView("main");
			List<Integer> list = movieMapper.getNewmovie_ids();
		try {	
			int index = 1;
			for (int item : list) {
				Movie newmovie = movieMapper.getNewmovie(item);
				List<String> newmovieGenres = movieMapper.getNewmovieGenres(item);
				List<String> newmovieContrys = movieMapper.getNewmovieCountrys(item);
				mav.addObject("newmovie"+index, newmovie);
				mav.addObject("newmovieGenres"+index, newmovieGenres);
				mav.addObject("newmovieCountrys"+index, newmovieContrys);
				
				index++;
			}
			
			List<Movie> movies = movieMapper.getTodaymovies();
			mav.addObject("todaymovies", movies);
			
			if(session.getAttribute("id") == null) {
				movies = movieMapper.getLovedmovies();
				mav.addObject("lovedmovies", movies);
			}else {
				movies = movieMapper.getCustommovies((int)session.getAttribute("id"));
				mav.addObject("custommovies", movies);
			}
			movies = movieMapper.getTopMovies();
			mav.addObject("topmovies", movies);
		
			return mav;
		}catch(Exception e) {
			return mav;
		}
	}
}
