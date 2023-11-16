package com.project.goldenticket.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.project.goldenticket.DTO.Article;

@Mapper
public interface ArticleMapper {

	@Select("SELECT A.*, C.categoryname, M.nickname FROM article A INNER JOIN article_category AC on A.id = AC. article_id INNER JOIN category C ON AC. category_id = C.id INNER JOIN member M ON A.mem_id = M.id WHERE A.id = #{id}")
	Article getById(@Param("id") int id);
	
	@Select("SELECT A.*, C.categoryname, M.nickname FROM article A INNER JOIN article_category AC on A.id = AC. article_id INNER JOIN category C ON AC. category_id = C.id INNER JOIN member M ON A.mem_id = M.id")
	List<Article> getAll();
}
