package com.goldenticket.DTO;

import lombok.Data;

@Data
public class Article {
	private int id;
	private int mem_id;
	private String title;
	private String content;
	private int hit;
	private String regdate;
	private String updatedate;
	private int likes;
	private int dislikes;
	private int category_id;
	private String categoryname;
	private String nickname;
}
