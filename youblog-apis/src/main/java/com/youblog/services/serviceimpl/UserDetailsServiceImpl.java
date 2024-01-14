package com.youblog.services.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.youblog.entities.UserDetailsEntity;
import com.youblog.payloads.GetUserRequest;
import com.youblog.payloads.UpdatePasswordRequest;
import com.youblog.payloads.UpdateUserRequest;
import com.youblog.payloads.UserDetailsRequest;
import com.youblog.repositories.UserDetailsRepository;
import com.youblog.services.UserDetailsService;
import com.youblog.utils.KeycloakUtils;
import com.youblog.utils.ResponseHandler;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserDetailsRepository userdetailsrepo;

	@Autowired
	KeycloakUtils keycloakutility;

	@Override
	public ResponseEntity<Map<String, Object>> createUser(UserDetailsRequest usrequest) {

		UserDetailsEntity checkflag = userdetailsrepo.checkemail(usrequest.getEmailId());

		if (checkflag == null) {

			Map<String, String> token = keycloakutility.getAdminToken();
			String accesstoken = token.get("access_token");
			keycloakutility.keycloakUserCreator(usrequest.getEmailId(), usrequest.getPassword(),
					usrequest.getFirstName(), usrequest.getLastName(), usrequest.getEmailId(), true, accesstoken);

			UserDetailsEntity use = new UserDetailsEntity();
			use.setEmailId(usrequest.getEmailId());
			use.setFirstName(usrequest.getFirstName());
			use.setLastName(usrequest.getLastName());
			use.setGender(usrequest.getGender());
			use.setPersonalNo(usrequest.getPersonalNo());
			use.setActiveFlag(true);
			userdetailsrepo.save(use);
			return ResponseHandler.response(null, "created", true);
		} else {
			return ResponseHandler.response(null, "email already present", false);
		}

	}

	@Override
	public ResponseEntity<Map<String, Object>> getUser(GetUserRequest usreq) {
		// TODO Auto-generated method stub
		if(usreq.getEmailId()!=null) {
			UserDetailsEntity getdetails = userdetailsrepo.getUserDetails(usreq.getEmailId());
			if(getdetails!=null) {
				Map<String, Object> hm = new HashMap<>();
				hm.put("firstName", getdetails.getFirstName());
				hm.put("lastName",getdetails.getLastName());
				hm.put("gender", getdetails.getGender());
				hm.put("emailId", getdetails.getEmailId());
				hm.put("personalNo", getdetails.getPersonalNo());
				hm.put("userId", getdetails.getUserId());
				return ResponseHandler.response(hm, "user details fetched", true);
			}
			else {
				return ResponseHandler.response(null, "No such user found", false);
			}
			
			
		}
		else {
			return ResponseHandler.response(null, "Please provide emailId", false);
		}
	
		
		
	}
	
	@Override
	public ResponseEntity<Map<String, Object>> updateUser(UpdateUserRequest usreq) {
		if(usreq.getUserId()!=null) {
			UserDetailsEntity getdetails = userdetailsrepo.updateUserDetails(usreq.getUserId());
			
			if(getdetails!=null) {
				getdetails.setFirstName(usreq.getFirstName());
				getdetails.setLastName(usreq.getLastName());
				getdetails.setGender(usreq.getGender());
				getdetails.setPersonalNo(usreq.getPersonalNo());
				getdetails.setActiveFlag(usreq.getActiveFlag());
				userdetailsrepo.save(getdetails);
				return ResponseHandler.response(null, "user updated successfully", true);
			}
			else {
				return ResponseHandler.response(null, "user cannot be updated", false);
			}
		
		}
		else {
			return ResponseHandler.response(null, "Please Provide userId", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> updateUserPassword(UpdatePasswordRequest usreq) {
		
		if(usreq.getEmailId()!=null) {
			if(usreq.getNewPassword().equals(usreq.getConfirmNewPassword())) {
				boolean validate = keycloakutility.validatePassword(usreq.getEmailId(), usreq.getCurrentPassword());
				
				if(validate) {
					Map<String, String> token = keycloakutility.getAdminToken();
					String accesstoken = token.get("access_token");
					ResponseEntity<Map<String, Object>> kyx = keycloakutility.updatePassword(usreq.getEmailId(),usreq.getNewPassword(),accesstoken);
					
					return ResponseHandler.response(null, kyx.getBody().get("message").toString(), Boolean.valueOf(kyx.getBody().get("status").toString()));
				}
				else {
					
					
					return ResponseHandler.response(null, "current password not matched", false);
				}
			
				
				
				
			}
			else {
				return ResponseHandler.response(null, "New password and confirm password should be same", false);
			}
		}
		else {
			return ResponseHandler.response(null, "Please Provide emailId", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> getUserList() {
		// TODO Auto-generated method stub
		ArrayList<Object[]> getuserlist = userdetailsrepo.getUserList();
		ArrayList<Map<String,Object>> userlist = new ArrayList<>() ;
	
		if(getuserlist==null) {
			return ResponseHandler.response(new ArrayList<>() ,"user list cannot be found", false);
		}
		else {
			getuserlist.forEach(ele->{
				Map<String,Object> res = new HashMap<>();
				res.put("emailId", ele[0].toString());	
				res.put("firstName", ele[1].toString());
//				res.put("lastName", ele[2].toString());
				res.put("gender", ele[2].toString());
//				res.put("personalNo", ele[6].toString());
//				res.put("activeFlag", ele[5].toString());
				userlist.add(res);
			});
			return ResponseHandler.response(userlist, "user list found", true);
		}
	
	}

}


