package com.goldenticket.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Article {
	private int id;
	private int mem_id;
	private String title;
	private String content;
	private int hit;
	private LocalDateTime regdate;
	private LocalDateTime updatedate;
	private int likes;
	private int dislikes;
	private int category_id;
	private String categoryname;
	private String nickname;
}
