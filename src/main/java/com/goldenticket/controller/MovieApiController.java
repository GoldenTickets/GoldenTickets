package com.goldenticket.controller;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goldenticket.DTO.Review;
import com.goldenticket.mapper.MovieMapper;
import com.goldenticket.service.MovieService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "영화 API", description = "영화 테이블과 관련된 API입니다.")

@RestController
@RequestMapping("/movies")
public class MovieApiController {

	@Autowired
	private MovieMapper movieMapper;
	
	@Autowired
	private MovieService movieService;

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
	@DeleteMapping("/review/{movie_id}")
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
	

	@Operation(summary = "북마크 추가", description = "클릭했을때 로그인을 하지않았다면 오른쪽에서 로그인섹션이 열립니다.")
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
	
	@Operation(summary = "북마크 삭제", description = "로그인을 하지않았다면 북마크 버튼이 default 상태인 북마크추가 버튼으로 나오기때문에"
			+ "로그인하기전에 북마크를 삭제할수없지만 버튼클릭이 아닌 자바스크립트로 요청할경우 로그인을 요구합니다. 로그인창이 오른쪽에서 로그인섹션이 열립니다.")
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
	
}
