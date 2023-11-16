package com.project.goldenticket.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.project.goldenticket.DTO.Company;

@Mapper
public interface CompanyMapper { // 매퍼 인터페이스

	@Insert("INSERT INTO company(company_name, company_address) VALUES(#{company.name}, #{company.address})")
	@Options(useGeneratedKeys=true, keyProperty="id") // insert문을 실행할 때 자동으로 생성된 id값이 자바 객체의 keyProperty에 넣어져 반환된다.
	int insert(@Param("company") Company company);
	
	@Select("SELECT * FROM company")
	@Results(id="CompanyMap", value={ // 이 매핑을 재활용하기 위해 id를 부여
		@Result(property="name", column="company_name"),
		@Result(property="address", column="company_address"),
		@Result(property="employeeList", column="id", many=@Many(select="com.project.goldenticket.EmployeeMapper.getByCompanyId")) //입력된 메서드로 얻은 데이터 줄줄이 달고옴 - 서브쿼리
	}) // id는 컬럼명과 변수명이 동일하기 때문에 별도의 매핑관계를 설정하지 않아도 자동 매핑 됨
	List<Company> getAll();
	
	@Select("SELECT * FROM company WHERE id=#{id}")
	@ResultMap("CompanyMap")
	Company getById(@Param("id") int id);
}
