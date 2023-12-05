package com.goldenticket.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.goldenticket.DTO.Movie;
import com.goldenticket.DTO.Review;
import com.goldenticket.mapper.MovieMapper;
import com.goldenticket.service.MovieService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;


@Controller
@RequestMapping("/movies")
public class MovieViewController {
	
	@Autowired
	private MovieService movieService;

	// 선택된 페이지, 장르, 정렬순서에 맞게 영화 리스트를 가져옵니다.
	@GetMapping("")
	public ModelAndView getMovies(@RequestParam(defaultValue = "1") int page, 
								  @RequestParam(defaultValue = "0") int genre, 
								  @RequestParam(defaultValue = "releasedate") String order) {
		
		ModelAndView mav = new ModelAndView("movieInfo_all");
		int pageSize = 15;
		int startRow = (page-1)*pageSize;
		RowBounds rowBounds = new RowBounds(startRow, pageSize); // 페이징 처리
		List<Movie> movies;
		int totalMovies;
		
		try {
			if (genre == 0) {
				movies = movieService.getAllMovies(rowBounds, order);
				totalMovies = movieService.totalMovies();
			}else {
				movies = movieService.getAllMovies(rowBounds, order, genre);
				totalMovies = movieService.totalMoviesByGenre(genre);
			}
			mav.addObject("order", order);
			mav.addObject("genre", genre);
			
			int totalPages = (int) Math.ceil((double) totalMovies / pageSize); // 전체 페이지 수 구하기
			
			mav.addObject("movies", movies);
			mav.addObject("currentPage", page);
	        mav.addObject("totalPages", totalPages);
	        
		
			return mav;
		}catch(Exception e) {
			e.printStackTrace();
			return mav;
		}
	}
	
	// 영화 id와 일치하는 영화를 리뷰들과 리뷰페이지와 함께 가져옵니다.
	@GetMapping("/{id}")
	public ModelAndView getMovie(@RequestParam(defaultValue = "1") int page,
								 @PathVariable int id,
								 HttpSession session) {
		try {
			movieService.updateHit(id); //조회수 1 증가
			ModelAndView mav=new ModelAndView("movie");
			
			int pageSize = 10;
			int startRow = (page-1)*pageSize;
			RowBounds rowBounds = new RowBounds(startRow, pageSize); // 페이징 처리
			
			int totalArticles = movieService.getTotalreviews(id);
			int totalPages = (int) Math.ceil((double) totalArticles / pageSize); // 전체 페이지 수 구하기
			mav.addObject("currentPage", page);
	        mav.addObject("totalPages", totalPages);
			
			Movie movie = movieService.getMovieById(id);
			List<String> movieActor= movieService.getMovieActors(id);
			
			int IsitBookmarked = 0; //북마크 여부의 기본값은 0
			
			Object session_id=session.getAttribute("id");
			if(session_id!=null) {
				int mem_id=(int)session_id;
				IsitBookmarked = movieService.IsitBookdmarkedById(id, mem_id);//북마크되어있는지 확인. 북마크되어있다면 1로 변경
			}
			
			List<Object> moviePhotoList = movieService.getMoviePhoto(id);
			
			mav.addObject("movie", movie);//영화 테이블에서 가져올 수 있는 컬럼
			mav.addObject("movieactor",movieActor);//영화배우목록
			mav.addObject("bookmarked",IsitBookmarked);//북마크여부
			mav.addObject("photofirst", (String)moviePhotoList.get(0));//첫번째 사진
			mav.addObject("photoremaining", (List)moviePhotoList.get(1));//첫번째 사진 제외한 나머지 사진
			mav.addObject("moviegenre",movieService.getMovieGenre(id));//영화 장르가져오기 (List)
			mav.addObject("movieplatform", movieService.getMoviePlatform(id));//영화 플랫폼가져오기
			mav.addObject("reviewlist", movieService.getMovieReview(id, rowBounds));//영화 리뷰가져오기
			
			return mav;

		}catch(Exception e) {
			e.printStackTrace();
			return new ModelAndView("redirect:/");

		}
	}
	

	// 선택된 페이지, 장르에 맞는 영화 순위를 가져옵니다.
	@GetMapping("/ranking")
	public ModelAndView getRanking(@RequestParam(defaultValue = "1") int page,  
								   @RequestParam(defaultValue = "0") int genre,
								   HttpSession session){ // page = 현재페이지, pageSize도 나중에 정할 수 있게 바꾸기
		
		List<List<Object>> moviebook = new ArrayList<List<Object>>();
			
		ModelAndView mav = new ModelAndView("movieRanking");
		int pageSize = 10;
		int startRow = (page-1)*pageSize;
		RowBounds rowBounds = new RowBounds(startRow, pageSize); // 페이징 처리
		List<Movie> movies;
		int totalMovies;
		try {
		if (genre == 0) {
			movies = movieService.getRanking(rowBounds);
			totalMovies = movieService.totalMovies();
		}else {
			movies = movieService.getRankingByGenre(rowBounds, genre);
			totalMovies = movieService.totalMoviesByGenre(genre);
		}
		mav.addObject("genre", genre);
		//mav.addObject("movies", movies);
		
		int totalPages = (int) Math.ceil((double) totalMovies / pageSize); // 전체 페이지 수 구하기
		mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);
	
        
        List<Integer> bookmarkList;
        Object session_id = session.getAttribute("id");
        
        int mem_id = 0;
        if(session_id!=null) {//로그인을 한 경우
    	   mem_id = (int)session_id;
        }


        bookmarkList = movieService.bookmarkListById(mem_id);
        List<Integer> bookmarked = new ArrayList();

        for(int index=0;index<movies.size();index++) {
        	List<Object> list= new ArrayList<>();
        	list.add(movies.get(index));
        	list.add(bookmarkList.contains(movies.get(index).getId())?1:0);
        	moviebook.add(list);
    	}

        mav.addObject("moviebook",moviebook);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return mav;

	}
	
	// 영화 검색
	@GetMapping("/search")
	public ModelAndView searchMovies(@RequestParam(name = "subject") String subject, @RequestParam(name = "genre", required = false) List<String> genre, @RequestParam(name = "keyword") String keyword, @RequestParam(defaultValue = "1") int page) {
		ModelAndView mav = new ModelAndView("movieInfo_result");
		
		int pageSize = 10;
		int startRow = (page-1)*pageSize;
		RowBounds rowBounds = new RowBounds(startRow, pageSize); // 페이징 처리
		List<Movie> movies;
		int totalMovies;
		/* -> 장르 List를 어떻게 할 것인지 고민하기
		if (genre == 0) {
			movies = movieService.getBySearch(rowBounds, subject, genre, keyword);
			totalMovies = movieService.totalmoviesBySearch( subject, genre, keyword);
		}else {
			movies = movieService.getBySearch(rowBounds, subject, genre, keyword);
			totalMovies = movieService.totalMoviesByGenre( subject, genre, keyword);
		}
		
		System.out.println(movies);
		mav.addObject("genre", genre);

		int totalPages = (int) Math.ceil((double) totalMovies / pageSize); // 전체 페이지 수 구하기
		
		mav.addObject("movies", movies);
		mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);
        */
	
		return mav;
	}
}
