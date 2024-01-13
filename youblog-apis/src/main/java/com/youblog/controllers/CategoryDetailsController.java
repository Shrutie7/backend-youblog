package com.youblog.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youblog.services.CategoryDetailsService;

@RestController
@RequestMapping("/category")
public class CategoryDetailsController {
	@Autowired
	private CategoryDetailsService categorydetailsservice;
	@GetMapping("/list")
	public ResponseEntity<Map<String,Object>> categoryList(){
		return categorydetailsservice.getCategoryList();
	}
		
}
