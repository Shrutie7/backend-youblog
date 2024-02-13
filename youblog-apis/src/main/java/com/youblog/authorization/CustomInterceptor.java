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
<<<<<<< HEAD
		System.out.println("pre handler is calling...");
		if (request.getMethod().equals("OPTIONS")) {
			return true;
		} else if (request.getRequestURI().equals("/captcha/get") || request.getRequestURI().equals("/captcha/validate")
				|| request.getRequestURI().contains("/post/get/media")
				|| request.getRequestURI().contains("/post/download/media/")
				|| request.getRequestURI().contains("/location/") || request.getRequestURI().contains("/users/create")
				|| request.getRequestURI().contains("/categorylist")) {
			return true;
		}
		if (request.getMethod().equals("OPTIONS")) {
			return true;
		} else if (request.getRequestURI().equals("/captcha/get")
				|| request.getRequestURI().equals("/captcha/validate")) {
=======
		if (request.getMethod().equals("OPTIONS")) {
			return true;
		} else if (request.getRequestURI().equals("/captcha/get")
				|| request.getRequestURI().equals("/captcha/validate")
				|| request.getRequestURI().contains("/location/")
				|| request.getRequestURI().contains("/categorylist")
				|| request.getRequestURI().contains("/users/create")
				|| request.getRequestURI().contains("/post/get/media/")
				|| request.getRequestURI().contains("/post/download/media/")) {
>>>>>>> 78bd50b183d0ba0cd28832d3703df934fb711ffb
			return true;
		} else if (request.getHeader("Authorization") != null) {
			String accessToken = request.getHeader("Authorization").substring(6);
			ResponseEntity<Map<String, Object>> validation = keycloakUtils.keycloakAuthorizeUser(accessToken);
			if (Boolean.valueOf(validation.getBody().get("status").toString())) {
				return true;
			} else {
<<<<<<< HEAD
				String responseData = "{\r\n" + "\"status\": false,\r\n" + "\"message\":\"Invalid Token\",\r\n"
						+ "    \"data\": null\r\n" + "}";
=======
				String responseData = "{\r\n" + "\t\"status\": false,\r\n" + "\t\"message\":\"Invalid Token\",\r\n"
						+ "\t\"data\": null\r\n" + "}";
>>>>>>> 78bd50b183d0ba0cd28832d3703df934fb711ffb
				response.getWriter().write(responseData);
				response.setHeader("Content-Type", "application/json");
				response.setStatus(401);
				return false;
			}
		} else {
			String responseData = "{\r\n" + "\t\"status\": false,\r\n" + "\t\"message\":\"Please provide the token\",\r\n"
					+ "\t\"data\": null\r\n" + "}";
			response.getWriter().write(responseData);
			response.setHeader("Content-Type", "application/json");
			response.setStatus(401);
			return false;
		}
	}

}
