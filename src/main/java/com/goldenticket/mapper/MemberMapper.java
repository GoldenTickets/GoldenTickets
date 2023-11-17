package com.goldenticket.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.goldenticket.DTO.Member;

@Mapper
public interface MemberMapper {
	
	//비밀번호가 일치하는지 확인
	@Select("SELECT password from Member WHERE email = #{email}")
	String logincheck(String email);
	
	//닉네임,id번호 가져오기
	@Select("SELECT id,nickname from Member WHERE email = #{email}")
	Member getMember(String nickname);
}
