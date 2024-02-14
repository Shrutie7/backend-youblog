package com.youblog.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.youblog.payloads.CityListRequest;
import com.youblog.payloads.GymAddressRequest;
import com.youblog.payloads.LocationAddressRequest;

public interface LocationDetailsService {

	public ResponseEntity<Map<String, Object>> stateList();

	public ResponseEntity<Map<String, Object>> cityList(CityListRequest cityListRequest);

	public ResponseEntity<Map<String, Object>> getLocationAddressList(LocationAddressRequest locationAddressRequest);

	public ResponseEntity<Map<String, Object>> getGymAddressList(GymAddressRequest gymAddressRequest);
}
