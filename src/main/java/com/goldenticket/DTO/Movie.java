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
	
	//평점을 구하기위해 SUM,COUNT를 반환받기위함
	private int rating_sum;//해당영화의 리뷰 평점의 총 합
	private int rating_count;//해당영화 리뷰의 총 갯수
}
