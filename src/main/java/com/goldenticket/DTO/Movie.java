package com.goldenticket.DTO;



import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class Movie {
	
	private String id;
	private String title;
	private String synopsis;
	private LocalDateTime releasedate;
	private String director;
	private String country;
	private int runningtime;
	private String poster;
	private String trailer;
	private double rating;
	private int hit;
	
	//조인해서 가져온 데이터
	private List<MoviePhoto> moviePhoto;
	private double avg_rating;
	private int ranking;
	private String photoname;
}
