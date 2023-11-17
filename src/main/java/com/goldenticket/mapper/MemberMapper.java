package com.goldenticket.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.goldenticket.DTO.Member;

@Mapper
public interface MemberMapper {

	@Insert("INSERT INTO member (email, password, name, nickname) VALUES (#{member.email}, #{member.password}, #{member.name}, #{member.nickname})")
	int signup(@Param("member") Member member);
	
	@Select("SELECT * FROM member WHERE email = #{member.email}")
	Member login(@Param("member") Member member);
}
