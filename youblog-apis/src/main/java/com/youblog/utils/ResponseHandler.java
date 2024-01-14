package com.youblog.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;

public class ResponseHandler {

	public static ResponseEntity<Map<String, Object>> response(Object data, String message, boolean status) {

		Map<String, Object> res = new HashMap<>();

		res.put("data", data);
		res.put("message", message);
		res.put("status", status);

		return ResponseEntity.ok(res);
	}

}
