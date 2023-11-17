package com.goldenticket.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Review {
	private int id; 
	private int movie_id;
	private String mem_id;
	private int rating;
	private String content;
	private LocalDateTime regdate;
	private int liked;//해당 리뷰에 대한 좋아요
	private int disliked;//해당 리뷰에 대한 싫어요
	
	//조인해서 가져온 작성자 nickname;
	private String nickname;

}
