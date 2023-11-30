package com.goldenticket.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

//@ControllerAdvice
@Tag(name = "영화 API", description = "영화 관련 API")
@RestController
@RequestMapping("/movies")
public class MovieController {

	@Autowired
	private MovieMapper movieMapper;
	
	@Autowired
	private MovieService movieService;

	@Operation(summary = "영화 리스트 가져오기", description = "선택된 페이지, 장르, 정렬순서에 맞게 영화 리스트를 가져옵니다.")
	@GetMapping("")
	public ModelAndView getMovies(@Parameter(description = "페이지") @RequestParam(defaultValue = "1") int page, 
								  @Parameter(description = "장르") @RequestParam(defaultValue = "0") int genre, 
								  @Parameter(description = "정렬순서") @RequestParam(defaultValue = "releasedate") String order) {
		
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
	
	@Operation(summary = "특정 영화 가져오기", description = "영화 id와 일치하는 영화를 리뷰들과 리뷰페이지와 함께 가져옵니다.")
	@GetMapping("/{id}")
	public ModelAndView getMovie(@Parameter(description = "페이지") @RequestParam(defaultValue = "1") int page,
								 @Parameter(description = "영화 id") @PathVariable int id,
								 @Parameter(description = "세션(로그인정보)") HttpSession session) {
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

	@Operation(summary = "리뷰 작성", description = "회원 정보를 조회하여 이 영화에 리뷰를 작성했는지 확인하고 작성하지 않았을 경우 리뷰를 작성합니다.")
	@PostMapping("/review/{id}")
	public ResponseEntity<String> createReview(@Parameter(description = "클라이언트에서 입력한 데이터를 담은 DTO") @RequestBody Review review,
											   @Parameter(description = "영화 id") @PathVariable int id,
											   @Parameter(description = "세션(로그인정보)") HttpSession session){
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

	@Operation(summary = "리뷰 삭제", description = "리뷰를 작성한 본인이 맞을 경우 리뷰를 삭제합니다.")
	@GetMapping("/review/{movie_id}")
	public ResponseEntity<String> deleteReview(@Parameter(description = "영화 id")@PathVariable int movie_id,
											   @Parameter(description = "세션(로그인정보)")HttpSession session){
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

	
	

	@Operation(summary = "영화 순위 가져오기", description = "선택된 페이지, 장르에 맞는 영화 순위를 가져옵니다.")
	@GetMapping("/ranking")
	public ModelAndView getRanking(@Parameter(description = "페이지") @RequestParam(defaultValue = "1") int page,  
								   @Parameter(description = "장르") @RequestParam(defaultValue = "0") int genre,
								   @Parameter(description = "세션(로그인정보)") HttpSession session){ // page = 현재페이지, pageSize도 나중에 정할 수 있게 바꾸기
		
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
	

	@Operation(summary = "북마크 추가", description = "")
	@PostMapping("/bookmark/{movie_id}")
	public ResponseEntity<String> createBookmark(@Parameter(description = "영화 id") @PathVariable int movie_id,
												 @Parameter(description = "세션(로그인정보)") HttpSession session) {
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
	
	@Operation(summary = "북마크 삭제", description = "")
	@DeleteMapping("/bookmark/{movie_id}")
	public ResponseEntity<String> deleteBookmark(@Parameter(description = "영화 id") @PathVariable int movie_id,
												 @Parameter(description = "세션(로그인정보)") HttpSession session) {
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
