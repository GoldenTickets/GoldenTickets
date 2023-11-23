package com.goldenticket.DTO;

import java.util.List;

import lombok.Data;

@Data
public class Member {
	private int id;
	private String email;
	private String password;
	private String name;
	private String nickname;
	
	//조인 컬럼
	private List<Integer> member_genre;
}
