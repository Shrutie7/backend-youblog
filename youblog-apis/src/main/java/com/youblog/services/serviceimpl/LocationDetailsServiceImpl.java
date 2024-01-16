package com.youblog.services.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.youblog.payloads.Citylistrequest;
import com.youblog.payloads.Gymaddressrequest;
import com.youblog.payloads.Locationaddressrequest;
import com.youblog.repositories.GymDetailsRepository;
import com.youblog.repositories.LocationDetailsRepository;
import com.youblog.services.LocationDetailsService;
import com.youblog.utils.ResponseHandler;

@Service
public class LocationDetailsServiceImpl implements LocationDetailsService{

	@Autowired
	LocationDetailsRepository locationdetailsrepo;
	
	@Autowired
	GymDetailsRepository gymdetailsrepo;
	
	@Override
	public ResponseEntity<Map<String, Object>> getstatelist() {
		
		
		Map<String, Object> data = new HashMap<>();
		ArrayList<Object[]> getstate = locationdetailsrepo.getstatelist();
		ArrayList<Map<String,Object>> statelist = new ArrayList<>();
		
		if(getstate == null ) {
			   data.put("stateList", new ArrayList<>());
			return ResponseHandler.response(data,"state details not found" , false);
		}
		else
			
			getstate.forEach(ele->{
				Map<String, Object> statelist1 = new HashMap<>();
				statelist1.put("stateName",ele[0].toString());
//				statelist1.put("stateName", ele.getState());
				
			statelist.add(statelist1);
			});
			   data.put("stateList", statelist);
			return ResponseHandler.response(data, "state list found", true);
		

	}
	@Override
	public ResponseEntity<Map<String, Object>> getcitylist(Citylistrequest usreq) {
		Map<String, Object> data = new HashMap<>();
		ArrayList<Object[]> getcity = locationdetailsrepo.getcitylist(usreq.getState());
		ArrayList<Map<String,Object>> citylist = new ArrayList<>();
		
		if(usreq.getState()!=null) {
			if(getcity==null) {
				  data.put("citylist", new ArrayList<>());
				return ResponseHandler.response(data, "city list not found", false);
			}
			else {
				
				getcity.forEach(e->{
					Map<String,Object> citylist1 = new HashMap<>();
					citylist1.put("city",e[0].toString());
					
					citylist.add(citylist1);
				});
				data.put("citylist", citylist);
				return ResponseHandler.response(data, "city list found", true);
			}
		}
		else {
			return ResponseHandler.response(null, "Please Provide state", false);
		}
		
	}
	@Override
	public ResponseEntity<Map<String, Object>> getlocationaddresslist(Locationaddressrequest usreq) {
		
		Map<String, Object> data = new HashMap<>();
		
		ArrayList<Object[]> getaddressList = locationdetailsrepo.getlocationaddress(usreq.getState(), usreq.getCity());
		ArrayList<Map<String,Object>> addresslist = new ArrayList<>();
		if(usreq.getState()!=null && usreq.getCity()!=null) {
			
			if(getaddressList==null)
			{
				 data.put("addressList", new ArrayList<>());
				return ResponseHandler.response(new ArrayList<>(), "Location Address list not found", false);
			}
			else {
				getaddressList.forEach(ele->{
					Map<String, Object> addressList1 = new HashMap<>();
					addressList1.put("locationId",ele[0].toString());
					addressList1.put("location_address", ele[1].toString());
					
					addresslist.add(addressList1);
				});
				
				data.put("addressList",addresslist);
				
				return ResponseHandler.response(data, "Location address list found", true);
			}
		}
		
		else {
			return ResponseHandler.response(null, "Please Provide city and state both", false);
		}
	}
	@Override
	public ResponseEntity<Map<String, Object>> getgymaddresslist(Gymaddressrequest usreq) {
		
		Map<String, Object> data = new HashMap<>();
		ArrayList<Object[]> getgymaddress = gymdetailsrepo.getgymaddresslist(usreq.getLocation_id());
		ArrayList<Map<String, Object>> gymaddresslist = new ArrayList<>();
	if(usreq.getLocation_id()!=null) {
		
		if(getgymaddress==null) {
			data.put("gymAddressList", new ArrayList<>());
			return ResponseHandler.response(data, "gym address list not found", false);
			
		}
		else {
			
			getgymaddress.forEach(ele->{
				Map<String, Object> gymaddresslist1 = new HashMap<>();
				gymaddresslist1.put("gymId", ele[0].toString());
				gymaddresslist1.put("gymName", ele[1].toString());
				gymaddresslist1.put("gymAddress", ele[2].toString());
				
				gymaddresslist.add(gymaddresslist1);
			});
			data.put("gymAddressList", gymaddresslist);
			
			return ResponseHandler.response(data, "gym address list found", true);
		}
	}
	else {
		return ResponseHandler.response(null, "Please provide locationId", false);
	}
	}





}
