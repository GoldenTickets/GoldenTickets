package com.goldenticket.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.goldenticket.DTO.Movie;
import com.goldenticket.mapper.MovieMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "메인 페이지", description = "메인페이지와 관련된 메서드들 모음")
@Controller
public class MainController {

	@Autowired
	private MovieMapper movieMapper;
	
	// 세션에 저장된 로그인 정보를 확인 후 그에 맞는 메인 페이지를 가져옵니다.
	@GetMapping("")
	public ModelAndView getAll(@Parameter(description = "세션(로그인정보)") HttpSession session){

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
	
	// @Scheduled 애너테이션을 이용하여 매일 자정에 오늘의 영화 테이블의 영화들을 갱신합니다.
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행되어 오늘의 영화 테이블을 갱신한다.
	@PostMapping("/todaymovie")
	public int saveNewtodaymovie() {
		List<Integer> list = movieMapper.getNewtodaymovie();
		movieMapper.deletetodaymovies();
		
		int index = 1;
		for (int item : list) {
			movieMapper.saveNewtodaymovie(index, item);
			index++;
		}
		return 0;
	}
}
