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
import com.youblog.payloads.TrainerListRequest;
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
			 ResponseEntity<Map<String, Object>> flag = keycloakutility.keycloakUserCreator(usrequest.getEmailId(), usrequest.getPassword(),
					usrequest.getFirstName(), usrequest.getLastName(), usrequest.getEmailId(), true, accesstoken);

			 if(!Boolean.valueOf(flag.getBody().get("status").toString())) {
				 
				 return ResponseHandler.response(null, flag.getBody().get("message").toString(), false);
			 }
			 try {
				 UserDetailsEntity use = new UserDetailsEntity();
					use.setEmailId(usrequest.getEmailId());
					use.setFirstName(usrequest.getFirstName());
					use.setLastName(usrequest.getLastName());
					use.setLocationId(usrequest.getLocationId());
					use.setParentUserId(usrequest.getParentUserId());
					use.setRoleId(usrequest.getRoleId());
					use.setUserName(usrequest.getUserName());
					use.setGender(usrequest.getGender());
					use.setActiveFlag(true);
					use.setCategoryId(usrequest.getCategoryId());
					use.setGymId(usrequest.getGymId());
					userdetailsrepo.save(use);
			 }
			catch(Exception e) {
//				e.printStackTrace();
				keycloakutility.keycloakUserDelete(usrequest.getEmailId(),accesstoken);
				
				return ResponseHandler.response(null, "user cannot be created because " + e.getLocalizedMessage(), false);
				
			}
			return ResponseHandler.response(null, "created", true);
		} else {
			return ResponseHandler.response(null, "email already present", false);
		}

	}

	@Override
	public ResponseEntity<Map<String, Object>> getUser(GetUserRequest usreq) {
		// TODO Auto-generated method stub
		if(usreq.getEmailId()!=null) {
			ArrayList<Object[]> getdetails = userdetailsrepo.getUserDetails(usreq.getEmailId());
			System.out.println(getdetails);
			if(getdetails!=null) {
				Map<String, Object> hm = new HashMap<>();
//				hm.put("firstName", getdetails.getFirstName());
//				hm.put("lastName",getdetails.getLastName());
//				hm.put("gender", getdetails.getGender());
//				hm.put("emailId", getdetails.getEmailId());
//				hm.put("userId", getdetails.getUserId());
//				hm.put("userName", getdetails.getUserName());
//				hm.put("roleId",getdetails.getRoleId());
				
				getdetails.forEach(ele->{
				
					
					hm.put("userName",ele[0]!=null?ele[0].toString():"N/A");
					hm.put("emailId",ele[1]!=null?ele[1].toString():"N/A");
					hm.put("firstName",ele[2]!=null?ele[2].toString():"N/A");
					hm.put("lastName",ele[3]!=null?ele[3].toString():"N/A");
					hm.put("roleId",ele[4]!=null?ele[4].toString():"");
					hm.put("gender",ele[5]!=null?ele[5].toString():"N/A");
					hm.put("roleName",ele[6]!=null?ele[6].toString():"N/A");
					hm.put("userId",ele[7]!=null?ele[7].toString():"N/A");
					hm.put("gymId",ele[8]!=null?ele[8].toString():"");
					
					Map<String,Object> parentdetails = new HashMap<>();
					
					if(ele[9]!=null) 
					{
						parentdetails.put("username", ele[10]!=null?ele[10].toString():"N/A");
						parentdetails.put("firstName",ele[11]!=null?ele[11].toString():"N/A");
						parentdetails.put("lastName",ele[12]!=null?ele[12].toString():"N/A");
						parentdetails.put("roleId",ele[13]!=null?ele[13].toString():"");
						
					}
					hm.put("parentUserDetails",parentdetails);
					hm.put("parentUserId", ele[9]!=null?ele[9].toString():"");
					
					Map<String,Object> locationDetails = new HashMap<>();
					
					locationDetails.put("locationId",ele[14]!=null?ele[14].toString():"");
					locationDetails.put("state",ele[15]!=null?ele[15].toString():"N/A");
					locationDetails.put("city",ele[16]!=null?ele[16].toString():"N/A");
					locationDetails.put("location",ele[17]!=null?ele[17].toString():"N/A");
					
					hm.put("locationDetails", locationDetails);
					
					Map<String,Object> planDetails = new HashMap<>();
					
					planDetails.put("planId",ele[18]!=null?ele[18].toString():"N/A");
					planDetails.put("planName",ele[19]!=null?ele[19].toString():"N/A");
					planDetails.put("planStartDate",ele[20]!=null?ele[20].toString():"N/A");
					
					hm.put("planDetails", planDetails);

					hm.put("gymtypeId", ele[21]!=null ? ele[21]:"");
					});
				
				
				
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
				getdetails.setUserName(usreq.getUserName());
				getdetails.setParentUserId(usreq.getParentUserId());//post approval of worklist
				getdetails.setLocationId(usreq.getLocationId());//post approval of worklist
				getdetails.setActiveFlag(usreq.getActiveFlag()); //post approval of worklist 
				getdetails.setGymId(usreq.getGymId());
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
		ArrayList<UserDetailsEntity> getuserlist = userdetailsrepo.getUserList();
		ArrayList<Map<String,Object>> userlist = new ArrayList<>() ;
	
		if(getuserlist==null) {
			return ResponseHandler.response(new ArrayList<>() ,"user list cannot be found", false);
		}
		else {
			getuserlist.forEach(ele->{
				Map<String,Object> res = new HashMap<>();
				res.put("emailId", ele.getEmailId());	
				res.put("firstName", ele.getFirstName());
				res.put("lastName", ele.getLastName());
				res.put("gender", ele.getGender());
				res.put("locationId",ele.getLocationId());
				res.put("parentUserId",ele.getParentUserId());
				res.put("roleId", ele.getRoleId());
				res.put("planId",ele.getPlanId());
				res.put("userName", ele.getUserName());
				userlist.add(res);
			});
			return ResponseHandler.response(userlist, "user list found", true);
		}
	
	}

	@Override
	public ResponseEntity<Map<String, Object>> getTrainerList(TrainerListRequest usreq) {
	
	
		if(usreq.getOwnerId()!=null) {
			
			
			ArrayList<Object[]> getTrainerList = userdetailsrepo.getTrainerList(usreq.getOwnerId());
			
			ArrayList<Map<String,Object>> trainerlist = new ArrayList<>() ;
			
			Map<String, Object> listOfTrainers = new HashMap<>();
			if(getTrainerList.size()==0) {
				listOfTrainers.put("listOfTrainers", trainerlist);
				
				return ResponseHandler.response(listOfTrainers,"trainer list not found", false);
			}
			else {
				
				getTrainerList.forEach(ele->{
					Map<String,Object> res = new HashMap<>();
					
					res.put("userId", ele[2]!=null ? ele[2].toString():"");
					
//					String fullName =  "";
//					fullName = ele.getFirstName() + " " + ele.getLastName();
					
					res.put("trainerName", ele[1]!=null ? ele[1].toString():"N/A");
					res.put("rating", ele[0]!=null ? ele[0].toString():"");
					res.put("categoryId",ele[3]!=null?ele[3].toString():"");
					res.put("categoryName",ele[4]!=null ? ele[4].toString():"" );
					trainerlist.add(res);
					
				});
				listOfTrainers.put("listOfTrainers", trainerlist);
				
				return ResponseHandler.response(listOfTrainers,"trainer list found", true);
			}
			
			
		
			
		}
		else {
			return ResponseHandler.response(null, "Please Provide owner Id", false);
		}
		
		
	
	}

}


