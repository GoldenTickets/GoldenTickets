package com.goldenticket.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.goldenticket.DTO.Movie;
import com.goldenticket.mapper.MovieMapper;


@RestController
@RequestMapping("")
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
	
	// 매일 자정에 실행되어 오늘의 영화 테이블을 갱신한다.
    @Scheduled(cron = "0 0 0 * * ?")
	@PostMapping("/newtodaymovie")
	public int saveNewtodaymovie() {
		List<Integer> list = movieMapper.getNewtodaymovie();
		System.out.println(list);
		movieMapper.deletetodaymovies();
		
		int index = 1;
		for (int item : list) {
			movieMapper.saveNewtodaymovie(index, item);
			index++;
		}
		return 0;
	}
}
