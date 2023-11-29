package com.goldenticket.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
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

//@ControllerAdvice
@RestController
@RequestMapping("/movie")
public class MovieController {

	@Autowired
	private MovieMapper movieMapper;
	
	@Autowired
	private MovieService movieService;

	@GetMapping("/{id}")
	public ModelAndView getAllMovies(@RequestParam(defaultValue = "1") int page,
									 @PathVariable int id,
									 HttpSession session) {
		try {
			movieService.updateHit(id); //조회수 1 증가
			ModelAndView mav=new ModelAndView("movie");
			
			int pageSize = 10;
			int startRow = (page-1)*pageSize;
			RowBounds rowBounds = new RowBounds(startRow, pageSize); // 페이징 처리
			
			int totalArticles = movieMapper.getTotalreviews(id);
			int totalPages = (int) Math.ceil((double) totalArticles / pageSize); // 전체 페이지 수 구하기
			mav.addObject("currentPage", page);
	        mav.addObject("totalPages", totalPages);
			
			Movie movie = movieService.getMovieById(id);
			List<String> movieActor= movieService.getMovieActors(id);
			
			int IsitBookmarked = 0;//북마크 여부의 기본값은 0
			
			Object session_id=session.getAttribute("id");
			if(session_id!=null) {
				int mem_id=(int)session_id;
				IsitBookmarked = movieService.IsitBookdmarkedById(id, mem_id);//북마크되어있는지 확인. 북마크되어있다면 1로 변경
			}
			
			List<Object> moviePhotoList = movieService.getMoviePhoto(id);
			
			mav.addObject("movie", movie);
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
	
	//리뷰작성기능. 회원당 리뷰 작성 횟수 1회 제한. (평점을 여러번 매길수없도록)
	@PostMapping("/submitreview/{id}")
	public ResponseEntity<String> reviewSubmit(@RequestBody Review review,
											   @PathVariable int id,
											   HttpSession session){
		try {
			int result = movieService.createMovieReview(review,id,(int)session.getAttribute("id"));

			if(result==2) {//해당 리뷰에대해 회원이 이미 작성한 리뷰가 있을경우
				return new ResponseEntity<>("duplicated",HttpStatus.OK);
			
			}else if(result==1) {//최종적으로 리뷰 작성에 성공했을경우
				return new ResponseEntity<>("success",HttpStatus.OK);
			}else{
				return new ResponseEntity<>("fail",HttpStatus.BAD_REQUEST);
			}
		}catch(NullPointerException e) {//로그인이 안되어있으면 위의 session.getAttriute를 하는과정에서 nullPointException 발생함
			e.printStackTrace();
			return new ResponseEntity<>("needLogin",HttpStatus.OK);
		}catch(Exception e) {//그 외의 이유로 회원가입이 실패했을경우 예외 반환
			e.printStackTrace();
			return new ResponseEntity<>("fail",HttpStatus.BAD_REQUEST);
		}
	}
	
	//리뷰삭제기능
	@GetMapping("/deleteReview/{movie_id}")
	public ResponseEntity<String> deleteReview(@PathVariable int movie_id,
												HttpSession session){
		try {
			movieService.deleteReview(movie_id, (int)session.getAttribute("id"));
			return new ResponseEntity("success",HttpStatus.OK);
		}catch(NullPointerException e) {
			return new ResponseEntity("needLogin",HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity("오류로 인해 삭제에 실패했습니다",HttpStatus.OK);
		}
	}

	@GetMapping("")
	public ModelAndView getAll(@RequestParam(defaultValue = "1") int page, 
							   @RequestParam(defaultValue = "0") int genre, 
							   @RequestParam(defaultValue = "releasedate") String order
							   ) { // page = 현재페이지, pageSize도 나중에 정할 수 있게 바꾸기
		
		ModelAndView mav = new ModelAndView("movieInfo_all");
		int pageSize = 15;
		int startRow = (page-1)*pageSize;
		RowBounds rowBounds = new RowBounds(startRow, pageSize); // 페이징 처리
		List<Movie> movies;
		System.out.println(order);
		int totalMovies;
		
		if (genre == 0) {
			movies = movieService.getAllMovies(rowBounds, order);
			totalMovies = movieMapper.totalMovies();
		}else {
			movies = movieService.getAllMovies(rowBounds, order, genre);
			totalMovies = movieMapper.totalMoviesByGenre(genre);
		}
		System.out.println(movies);
		mav.addObject("order", order);
		mav.addObject("genre", genre);
		
		int totalPages = (int) Math.ceil((double) totalMovies / pageSize); // 전체 페이지 수 구하기
		
		mav.addObject("movies", movies);
		mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);
        
	
		return mav;
	}
	

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
			movies = movieMapper.getRanking(rowBounds);
			totalMovies = movieMapper.totalMovies();
		}else {
			movies = movieMapper.getRankingByGenre(rowBounds, genre);
			totalMovies = movieMapper.totalMoviesByGenre(genre);
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
	

	//북마크 추가
	@GetMapping("/bookmark/{movie_id}")
	public ResponseEntity<String> createBookmark(@PathVariable int movie_id,
												 HttpSession session) {
		try {
			
			int mem_id;
			Object Session_id = session.getAttribute("id");
			if(Session_id==null) {
				return new ResponseEntity<>("needLogin",HttpStatus.BAD_REQUEST);
			}else{
				mem_id = (int)Session_id;
				movieService.createBookmark(movie_id,mem_id);
				return new ResponseEntity<>("success",HttpStatus.OK);
			}
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("fail",HttpStatus.BAD_REQUEST);
		}
	}
	
	//북마크 삭제
		@GetMapping("/deletebookmark/{movie_id}")
		public ResponseEntity<String> deleteBookmark(@PathVariable int movie_id,
													 HttpSession session) {
			try {
				int mem_id;
				Object Session_id = session.getAttribute("id");
				if(Session_id==null) {
					return new ResponseEntity<>("needLogin",HttpStatus.BAD_REQUEST);
				}else{
					mem_id = (int)Session_id;
					movieService.deleteBookmark(movie_id,mem_id);
					return new ResponseEntity<>("success",HttpStatus.OK);
				}
			}catch(Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>("fail",HttpStatus.BAD_REQUEST);
			}
		}
		
	// 영화 검색
	/*@GetMapping("/search")
	public ModelAndView searchMovies(@RequestParam(name = "subject") String subject, @RequestParam(name = "genre", required = false) List<String> genre, @RequestParam(name = "keyword") String keyword, @RequestParam(defaultValue = "1") int page) {
		ModelAndView mav = new ModelAndView("movieInfo_result");
		
		int pageSize = 10;
		int startRow = (page-1)*pageSize;
		RowBounds rowBounds = new RowBounds(startRow, pageSize); // 페이징 처리
		List<Movie> movies;
		int totalMovies;
		
		if (genre == 0) {
			movies = movieMapper.getBySearch(rowBounds, order);
			totalMovies = movieMapper.totalmoviesBySearch();
		}else {
			movies = movieMapper.getAllMoviesByGenre(rowBounds, order, genre);
			totalMovies = movieMapper.totalMoviesByGenre(genre);
		}
		System.out.println(movies);
		mav.addObject("order", order);
		mav.addObject("genre", genre);
		
		int totalPages = (int) Math.ceil((double) totalMovies / pageSize); // 전체 페이지 수 구하기
		
		mav.addObject("movies", movies);
		mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);
        
	
		return mav;
	}*/
}
