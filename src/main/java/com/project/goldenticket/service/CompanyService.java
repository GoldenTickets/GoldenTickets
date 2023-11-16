package com.project.goldenticket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.goldenticket.DTO.Company;
import com.project.goldenticket.mapper.CompanyMapper;
import com.project.goldenticket.mapper.EmployeeMapper;

@Service
public class CompanyService {
	
	@Autowired
	private CompanyMapper companyMapper;
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	public List<Company> getAll() {
		List<Company> companyList = companyMapper.getAll();
		if (companyList != null && companyList.size() > 0) { // 컴퍼니 목록이 있을 경우
			for (Company company : companyList) { // 컴퍼니 목록의 루프를 돌면서
				company.setEmployeeList(employeeMapper.getByCompanyId(company.getId()));
			} // 임플로이 목록을 셋팅한다
		}
		return companyList;
	};
	
	@Transactional(rollbackFor=Exception.class) // 이 API 내에서 예외가 발생하면 이 API에서 일어난 DB 조작은 전부 롤백된다. 옆에 괄호는 득정 예외 지목하는 것 이 예외시 바로 롤백
	public Company add(Company company) {
		companyMapper.insert(company);
		// add company into legacy system 기존의 레거시 시스템에도 데이터 추가한다는 가정 이때 예외가 발생하면 앞에 코드까지 롤백 시키기 위해
		if (true) {
			throw new RuntimeException("Legacy Exception");
		}
		return company;
	}
}
