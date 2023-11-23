package com.goldenticket.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Review {
	private int id; 
	private int movie_id;
	private int mem_id;
	private int rating;
	private String content;
	private LocalDateTime regdate;
	private int liked;//해당 리뷰에 대한 좋아요
	private int disliked;//해당 리뷰에 대한 싫어요
	
	//조인 컬럼;
	private String nickname;
	private String title;

}
