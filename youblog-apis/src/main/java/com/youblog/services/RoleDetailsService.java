package com.youblog.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface RoleDetailsService {
	public ResponseEntity<Map<String,Object>> getRoleList();
}
