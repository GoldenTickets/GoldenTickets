package com.goldenticket.controller;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public ModelAndView getAll(@RequestParam(defaultValue = "1") int page) { // page = 현재페이지, pageSize도 나중에 정할 수 있게 바꾸기
		
		ModelAndView mav = new ModelAndView("movieInfo_all");
		int pageSize = 12;
		int startRow = (page-1)*pageSize;
		RowBounds rowBounds = new RowBounds(startRow, pageSize); // 페이징 처리
		List<Movie> movies = movieMapper.getAll(rowBounds);
		System.out.println(movies);
		
		int totalMovies = movieMapper.totalMovies();
		int totalPages = (int) Math.ceil((double) totalMovies / pageSize); // 전체 페이지 수 구하기
		mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);
		
		return mav;
	}
}
