package com.youblog.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.youblog.payloads.Citylistrequest;
import com.youblog.payloads.Gymaddressrequest;
import com.youblog.payloads.Locationaddressrequest;

public interface LocationDetailsService {

	
	public ResponseEntity<Map<String,Object>> getstatelist();
	
	public ResponseEntity<Map<String,Object>> getcitylist(Citylistrequest usreq);

	
	public ResponseEntity<Map<String,Object>> getlocationaddresslist(Locationaddressrequest usreq);
	
	public ResponseEntity<Map<String,Object>> getgymaddresslist(Gymaddressrequest usreq);
}
