package com.project.goldenticket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.project.goldenticket.DTO.Company;
import com.project.goldenticket.mapper.CompanyMapper;
import com.project.goldenticket.service.CompanyService;

@RestController
@RequestMapping("/company")
public class CompanyController {
	
	@Autowired
	private CompanyMapper companyMapper;
	
	@Autowired
	private CompanyService companyService;
	
	@PostMapping("")
	public Company post(@RequestBody Company company) {
		//companyMapper.insert(company);
		companyService.add(company);
		return company;
	}

	@GetMapping("")
	public List<Company> getAll() {
		return companyMapper.getAll();
	}
	/*
	@GetMapping("/{id}")
	public Company getById(@PathVariable("id") int id) {
		return companyMapper.getById(id);
	}
	*/
	@GetMapping("/{id}")
	public ModelAndView getById(@PathVariable("id") int id) {
		Company company = companyMapper.getById(id);
		ModelAndView mav = new ModelAndView("article");
		mav.addObject("article", company);
		return mav;
	}
}