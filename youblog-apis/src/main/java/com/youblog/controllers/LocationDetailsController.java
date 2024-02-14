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

import com.youblog.payloads.CityListRequest;
import com.youblog.payloads.GymAddressRequest;
import com.youblog.payloads.LocationAddressRequest;
import com.youblog.services.LocationDetailsService;

@RestController
@RequestMapping("/location")
@CrossOrigin(origins = "*")
public class LocationDetailsController {

	@Autowired
	private LocationDetailsService locationDetailsService;

	@GetMapping("/state")
	public ResponseEntity<Map<String, Object>> stateList() {
		return locationDetailsService.stateList();
	}

	@PostMapping("/city")
	public ResponseEntity<Map<String, Object>> cityList(@RequestBody CityListRequest cityListRequest) {
		return locationDetailsService.cityList(cityListRequest);
	}

	@PostMapping("/address")
	public ResponseEntity<Map<String, Object>> getLocationAddressList(
			@RequestBody LocationAddressRequest locationAddressRequest) {
		return locationDetailsService.getLocationAddressList(locationAddressRequest);
	}

	@PostMapping("/gymaddress")
	public ResponseEntity<Map<String, Object>> getGymAddressList(@RequestBody GymAddressRequest gymAddressRequest) {
		return locationDetailsService.getGymAddressList(gymAddressRequest);
	}
}
