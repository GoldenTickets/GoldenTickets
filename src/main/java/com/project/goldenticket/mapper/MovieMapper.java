package com.project.goldenticket.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.project.goldenticket.DTO.Movie;

@Mapper
public interface MovieMapper {
	
	@Select("SELECT * FROM movie WHERE movie_id=#{movie_id}")
	Movie getById(@Param("movie_id") int movie_id);
}
