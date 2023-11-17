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
	public ModelAndView getAll() {
		
		ModelAndView mav = new ModelAndView("movieInfo_all");
		return mav;
	}
}
