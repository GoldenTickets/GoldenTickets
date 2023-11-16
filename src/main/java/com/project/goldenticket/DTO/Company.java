package com.project.goldenticket.DTO;

import java.util.List;

import lombok.Data;

@Data // 롬복에서 지원하는 Getter와 Setter를 자동으로 생성해주는 어노테이션
public class Company { // 인터페이스에서 테이블과 매핑할 자바 객체
	private int id;
	private String name;
	private String address;
	private List<Employee> employeeList;
}
