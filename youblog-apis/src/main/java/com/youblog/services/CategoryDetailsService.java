package com.youblog.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface CategoryDetailsService {

	public ResponseEntity<Map<String,Object>> getCategoryList();
}
