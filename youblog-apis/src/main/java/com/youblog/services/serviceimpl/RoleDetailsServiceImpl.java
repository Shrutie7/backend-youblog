package com.youblog.services.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.youblog.entities.RoleDetailsEntity;
import com.youblog.repositories.RoleDetailsRepository;
import com.youblog.services.RoleDetailsService;
import com.youblog.utils.ResponseHandler;

@Service
public class RoleDetailsServiceImpl implements RoleDetailsService{
	@Autowired
	RoleDetailsRepository roledetailsrepo;
	
	@Override
	public ResponseEntity<Map<String, Object>> getRoleList() {
		Map<String, Object> data = new HashMap<>();
		
		ArrayList<RoleDetailsEntity> getrolelist = roledetailsrepo.getRoleList();
		ArrayList<Map<String,Object>> rolelist = new ArrayList<>();
		
		if(getrolelist == null ) {
			 data.put("roleDetailsList", new ArrayList<>());
			return ResponseHandler.response(data, "Role List not found", false);
		}
		else {
			
		   getrolelist.forEach(ele->{
				Map<String,Object> res = new HashMap<>();
				res.put("roleId", ele.getRole_id());
				res.put("roleName", ele.getRole_name());
				
				rolelist.add(res);
			});
		   data.put("roleDetailsList", rolelist);
			return ResponseHandler.response(data, "role list found", true);
			
		}
		
	}
}
