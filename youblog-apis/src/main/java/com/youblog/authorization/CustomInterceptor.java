package com.youblog.authorization;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;

import com.youblog.utils.KeycloakUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "*")
@Component
public class CustomInterceptor implements HandlerInterceptor {

	@Autowired
	private KeycloakUtils keycloakUtils;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (request.getMethod().equals("OPTIONS")) {
			return true;
		} else if (request.getHeader("Authorization") != null) {
			String accessToken = request.getHeader("Authorization").substring(6);
			ResponseEntity<Map<String, Object>> validation = keycloakUtils.keycloakAuthorizeUser(accessToken);
			if (Boolean.valueOf(validation.getBody().get("status").toString())) {
				return true;
			} else {
				String responseData = "{\r\n" + "\t\"status\": false,\r\n" + "\t\"message\":\"Invalid Token\",\r\n"
						+ "\t\"data\": null\r\n" + "}";
				response.getWriter().write(responseData);
				response.setHeader("Content-Type", "application/json");
				response.setStatus(401);
				return false;
			}
		} else if (request.getRequestURI().equals("/captcha/get") || request.getRequestURI().equals("/captcha/validate")
				|| request.getRequestURI().contains("/location/")
				|| request.getRequestURI().contains("/user/category/list")
				|| request.getRequestURI().contains("/users/create")
				|| request.getRequestURI().contains("/post/get/media/")
				|| request.getRequestURI().contains("/post/download/media/")
				|| request.getRequestURI().contains("/class/")) {
			return true;
		} else {
			String responseData = "{\r\n" + "\t\"status\": false,\r\n"
					+ "\t\"message\":\"Please provide the token\",\r\n" + "\t\"data\": null\r\n" + "}";
			response.getWriter().write(responseData);
			response.setHeader("Content-Type", "application/json");
			response.setStatus(401);
			return false;
		}
	}

}
