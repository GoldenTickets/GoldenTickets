package com.project.goldenticket.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.project.goldenticket.DTO.Employee;

@Mapper
public interface EmployeeMapper {
	@Insert("INSERT INTO employee(company_id, employee_name, employee_address) VALUES(#{employee.companyId}, #{employee.name}, #{employee.address})")
	@Options(useGeneratedKeys=true, keyProperty="id") // insert문을 실행할 때 자동으로 생성된 id값이 자바 객체의 keyProperty에 넣어져 반환된다.
	int insert(@Param("employee") Employee employee);
	
	@Select("SELECT * FROM employee")
	@Results(id="EmployeeMap", value={ // 이 매핑을 재활용하기 위해 id를 부여
		@Result(property="name", column="employee_name"),
		@Result(property="address", column="employee_address")
	}) // id는 컬럼명과 변수명이 동일하기 때문에 별도의 매핑관계를 설정하지 않아도 자동 매핑 됨
	List<Employee> getAll();
	
	@Select("SELECT * FROM employee WHERE id=#{id}")
	@ResultMap("EmployeeMap")
	Employee getById(@Param("id") int id);
	
	@Select("SELECT * FROM employee WHERE company_id=#{companyId}")
	@ResultMap("EmployeeMap")
	List<Employee> getByCompanyId(@Param("companyId") int companyId);
}
