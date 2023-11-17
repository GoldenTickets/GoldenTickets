package com.goldenticket.DTO;

import lombok.Data;

@Data
public class Member {
	private int id;
	private String email;
	private String password;
	private String name;
	private String nickname;
}
