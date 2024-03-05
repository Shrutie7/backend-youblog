package com.youblog.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.youblog.payloads.CategoryListRequest;

public interface CategoryDetailsService {

	public ResponseEntity<Map<String, Object>> categoryList();

	public ResponseEntity<Map<String, Object>> userCategoryList(CategoryListRequest categoryListRequest);

	public ResponseEntity<Map<String, Object>> categoryListFilter();
}
