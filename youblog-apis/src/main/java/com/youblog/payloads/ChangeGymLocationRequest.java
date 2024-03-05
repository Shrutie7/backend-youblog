package com.youblog.payloads;

import java.util.Map;

import org.json.JSONObject;

import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeGymLocationRequest {

	private Long userId;

	private Long roleId;

	private Long locationId;
	
	private Long gymId;

	public Map<String, Object> toMap() {
		return new JSONObject(JacksonUtil.toString(this)).toMap();
	}
}
