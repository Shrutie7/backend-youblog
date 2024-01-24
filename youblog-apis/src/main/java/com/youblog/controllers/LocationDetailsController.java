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

import com.youblog.payloads.Citylistrequest;
import com.youblog.payloads.Gymaddressrequest;
import com.youblog.payloads.Locationaddressrequest;
import com.youblog.services.LocationDetailsService;

@RestController
@RequestMapping("/location")
@CrossOrigin(origins = "*")
public class LocationDetailsController {
	@Autowired
	private LocationDetailsService locationdetailsservice;
	@GetMapping("/state")
	public ResponseEntity<Map<String,Object>> stateList(){
		return locationdetailsservice.getstatelist();
		}
	
	@PostMapping("/city")
	public ResponseEntity<Map<String,Object>> cityList(@RequestBody Citylistrequest usreq){
		return locationdetailsservice.getcitylist(usreq);
		}
	
	@PostMapping("/address")
	public ResponseEntity<Map<String,Object>> addressList(@RequestBody Locationaddressrequest usreq){
		return locationdetailsservice.getlocationaddresslist(usreq);
		}
	
	@PostMapping("/gymaddress")
	public ResponseEntity<Map<String, Object>> gymaddressList(@RequestBody Gymaddressrequest usreq){
		return locationdetailsservice.getgymaddresslist(usreq);
	}
}
