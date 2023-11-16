package com.project.goldenticket.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.project.goldenticket.DTO.Reply;

@Mapper
public interface ReplyMapper {

	@Select("SELECT R.*,M.nickname FROM reply R INNER JOIN member M ON R.mem_id = M.id WHERE R.article_id = #{id}")
	List<Reply> getByArticle_id(@Param("id") int id);
}
