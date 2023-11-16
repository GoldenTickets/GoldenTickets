package com.project.goldenticket.DTO;

import java.sql.Date;

import lombok.Data;

@Data
public class Movie {
	private int movie_id;
	private String movie_title;
	private String movie_synopsis;
	private Date movie_release;
	private String movie_director;
	private String movie_runningtime;
	private String movie_poster;
	private String movie_trailer;
	private String movie_photo;
}
