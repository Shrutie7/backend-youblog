package com.youblog.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface CaptchaService {

	public ResponseEntity<Map<String, Object>> getCaptcha();

	public ResponseEntity<Map<String, Object>> validateCaptcha(String captchaData);

}
