package com.youblog.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youblog.services.CaptchaService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/captcha")
public class CaptchaController {

	@Autowired
	private CaptchaService captchaService;

	@GetMapping("/get")
	private ResponseEntity<Map<String, Object>> getCaptcha() {
		return captchaService.getCaptcha();
	}

	@PostMapping("/validate")
	private ResponseEntity<Map<String, Object>> validateCaptcha(@RequestBody String captchaData) {
		return captchaService.validateCaptcha(captchaData);
	}
}
