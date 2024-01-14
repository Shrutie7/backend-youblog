package com.youblog.services.serviceimpl;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.youblog.services.CaptchaService;
import com.youblog.utils.CaptchaUtilities;
import com.youblog.utils.ResponseHandler;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CaptchaServiceImpl implements CaptchaService {

	@Autowired
	private CaptchaUtilities captchaUtilities;

	@Override
	public ResponseEntity<Map<String, Object>> getCaptcha() {
		String[] data = captchaUtilities.generateCaptchaImage();
		Base64.Encoder enc = Base64.getEncoder();
		String encode = enc.encodeToString(data[2].getBytes());
		Map<String, Object> obj = new HashMap<>();
		obj.put("captchaImage", data[0]);
		obj.put("captchaId", data[1]);
		obj.put("captchaEncoded", encode);
		return ResponseHandler.response(obj,"Capatcha generated successfully.", true);
	}

	@Override
	public ResponseEntity<Map<String, Object>> validateCaptcha(String captchaData) {
		System.out.println(captchaData);
		JSONObject request = new JSONObject(captchaData);
		if (request.get("captchaId") == null) {
			return ResponseHandler.response(null,"Please Provide Captcha Id", false);
		}
		if (request.get("captchaAnswer") == null) {
			return ResponseHandler.response(null,"Please Provide Captcha Answer", false);
		}
		Map<String, String> captcha = captchaUtilities.storeAnswerInMap(null, null);
		if (captcha.get(request.get("captchaId")) != null) {
			String finalCaptcha = captcha.get(request.get("captchaId")).toString();
			if (finalCaptcha != null) {
				log.debug("Login to authenticate..." + finalCaptcha);
				if (finalCaptcha.equals(request.get("captchaAnswer"))) {
					captcha.remove(request.get("captchaId"));
					return ResponseHandler.response(null,"Validated Successfully", true);
				} else {
					captcha.remove(request.get("captchaId"));
					return ResponseHandler.response(null,"Please Provide Valid Captcha", false);
				}
			} else {
				captcha.remove(request.get("captchaId"));
				return ResponseHandler.response(null,"Please Provide new Captcha", false);
			}
		}else {
			return ResponseHandler.response(null,"Please Provide new Captcha", false);
		}
	}

//	@Scheduled(cron = "0 30 * * * *")

}
