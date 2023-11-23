package com.goldenticket.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Reply {
	private int id;
	private int article_id;
	private int mem_id;
	private String content;
	private int likes;
	private int dislikes;
	private LocalDateTime regdate;
	private LocalDateTime updatedate;
	private String nickname;
	
	//조인 컬럼
	private String title;
}
