package com.goldenticket.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

	//회원가입
	@Insert("INSERT INTO member (email,password,nickname,name) VALUES (#{email},#{password},#{nickname},#{name})")
	int createMember(Member member);
	
	//회원가입선호하는장르
	@Insert("INSERT INTO member_genre (genre_id,mem_id) VALUES (#{genre_id},#{id})")
	int setMemberGenre(@Param("genre_id")int genre_id,@Param("id")int id);
}
