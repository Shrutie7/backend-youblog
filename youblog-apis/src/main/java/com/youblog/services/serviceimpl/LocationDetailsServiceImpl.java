package com.youblog.services.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.youblog.payloads.CityListRequest;
import com.youblog.payloads.GymAddressRequest;
import com.youblog.payloads.LocationAddressRequest;
import com.youblog.repositories.GymDetailsRepository;
import com.youblog.repositories.LocationDetailsRepository;
import com.youblog.services.LocationDetailsService;
import com.youblog.utils.ResponseHandler;

@Service
public class LocationDetailsServiceImpl implements LocationDetailsService {

	@Autowired
	LocationDetailsRepository locationDetailsRepository;

	@Autowired
	GymDetailsRepository gymDetailsRepository;

	@Override
	public ResponseEntity<Map<String, Object>> stateList() {

		Map<String, Object> data = new HashMap<>();
		ArrayList<Object[]> getstate = locationDetailsRepository.getStateList();
		ArrayList<Map<String, Object>> statelist = new ArrayList<>();

		if (getstate == null) {
			data.put("stateList", new ArrayList<>());
			return ResponseHandler.response(data, "state details not found", false);
		} else

			getstate.forEach(ele -> {
				Map<String, Object> statelist1 = new HashMap<>();
				statelist1.put("stateName", ele[0].toString());
//				statelist1.put("stateName", ele.getState());

				statelist.add(statelist1);
			});
		data.put("stateList", statelist);
		return ResponseHandler.response(data, "state list found", true);

	}

	@Override
	public ResponseEntity<Map<String, Object>> cityList(CityListRequest cityListRequest) {
		if (cityListRequest.getState() != null) {
			Map<String, Object> data = new HashMap<>();
			ArrayList<Object[]> getcity = locationDetailsRepository.getCityList(cityListRequest.getState());
			ArrayList<Map<String, Object>> citylist = new ArrayList<>();
			if (getcity == null) {
				data.put("citylist", new ArrayList<>());
				return ResponseHandler.response(data, "city list not found", false);
			} else {

				getcity.forEach(e -> {
					Map<String, Object> citylist1 = new HashMap<>();
					citylist1.put("city", e[0].toString());

					citylist.add(citylist1);
				});
				data.put("citylist", citylist);
				return ResponseHandler.response(data, "city list found", true);
			}
		} else {
			return ResponseHandler.response(null, "Please Provide state", false);
		}

	}

	@Override
	public ResponseEntity<Map<String, Object>> getLocationAddressList(LocationAddressRequest locationAddressRequest) {
//		if(!locationAddressRequest.getState().isEmpty() && !locationAddressRequest.getCity().isEmpty())

		if (!locationAddressRequest.getState().isEmpty() && !locationAddressRequest.getCity().isEmpty()) {
			System.out.println(locationAddressRequest.getState());
			System.out.println(locationAddressRequest.getCity());
			Map<String, Object> data = new HashMap<>();
			ArrayList<Object[]> getaddressList = locationDetailsRepository
					.getLocationAddress(locationAddressRequest.getState(), locationAddressRequest.getCity());
			ArrayList<Map<String, Object>> addresslist = new ArrayList<>();

			if (getaddressList == null) {
				data.put("addressList", new ArrayList<>());
				return ResponseHandler.response(new ArrayList<>(), "Location Address list not found", false);
			} else {
				getaddressList.forEach(ele -> {
					Map<String, Object> addressList1 = new HashMap<>();
					addressList1.put("locationId", ele[0].toString());
					addressList1.put("locationAddress", ele[1].toString());

					addresslist.add(addressList1);
				});

				data.put("addressList", addresslist);

				return ResponseHandler.response(data, "Location address list found", true);
			}
		}

		else {
			return ResponseHandler.response(null, "Please Provide city and state both", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> getGymAddressList(GymAddressRequest gymAddressRequest) {

		if (gymAddressRequest.getLocationId() != null) {
			Map<String, Object> data = new HashMap<>();
			ArrayList<Object[]> getgymaddress = gymDetailsRepository
					.getGymAddressList(gymAddressRequest.getLocationId());
			ArrayList<Map<String, Object>> gymaddresslist = new ArrayList<>();
			if (getgymaddress == null) {
				data.put("gymAddressList", new ArrayList<>());
				return ResponseHandler.response(data, "gym address list not found", false);

			} else {

				getgymaddress.forEach(ele -> {
					Map<String, Object> gymaddresslist1 = new HashMap<>();
					gymaddresslist1.put("gymId", ele[0].toString());
					gymaddresslist1.put("gymName", ele[1].toString());
					gymaddresslist1.put("gymAddress", ele[2].toString());
					gymaddresslist1.put("ownerId", ele[3].toString());
					gymaddresslist.add(gymaddresslist1);
				});
				data.put("gymAddressList", gymaddresslist);

				return ResponseHandler.response(data, "gym address list found", true);
			}
		} else {
			return ResponseHandler.response(null, "Please provide locationId", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> getGymAddressListFilter(GymAddressRequest gymAddressRequest) {
		if (gymAddressRequest.getLocationId() != null) {
			Map<String, Object> data = new HashMap<>();
			List<Object[]> getgymaddress = gymDetailsRepository
					.getgymAddressListFilter(gymAddressRequest.getLocationId());
			ArrayList<Map<String, Object>> gymaddresslist = new ArrayList<>();
			if (getgymaddress == null) {
				data.put("gymAddressList", new ArrayList<>());
				return ResponseHandler.response(data, "gym address list not found", false);

			} else {

				getgymaddress.forEach(ele -> {
					Map<String, Object> gymaddresslist1 = new HashMap<>();
					gymaddresslist1.put("gymId", ele[0].toString());
					gymaddresslist1.put("gymName", ele[1].toString());
					gymaddresslist1.put("gymAddress", ele[2].toString());
					gymaddresslist1.put("ownerId", ele[3].toString());
					gymaddresslist.add(gymaddresslist1);
				});
				data.put("gymAddressList", gymaddresslist);

				return ResponseHandler.response(data, "gym address list found", true);
			}
		} else {
			return ResponseHandler.response(null, "Please provide locationId", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> getLocationAddressListFilter(
			LocationAddressRequest locationAddressRequest) {
		if (!locationAddressRequest.getState().isEmpty() && !locationAddressRequest.getCity().isEmpty()) {
			System.out.println(locationAddressRequest.getState());
			System.out.println(locationAddressRequest.getCity());
			Map<String, Object> data = new HashMap<>();
			ArrayList<Object[]> getaddressList = locationDetailsRepository
					.getLocationAddressFilter(locationAddressRequest.getState(), locationAddressRequest.getCity());
			ArrayList<Map<String, Object>> addresslist = new ArrayList<>();

			if (getaddressList == null) {
				data.put("addressList", new ArrayList<>());
				return ResponseHandler.response(new ArrayList<>(), "Location Address list not found", false);
			} else {
				getaddressList.forEach(ele -> {
					Map<String, Object> addressList1 = new HashMap<>();
					addressList1.put("locationId", ele[0].toString());
					addressList1.put("locationAddress", ele[1].toString());

					addresslist.add(addressList1);
				});

				data.put("addressList", addresslist);

				return ResponseHandler.response(data, "Location address list found", true);
			}
		}

		else {
			return ResponseHandler.response(null, "Please Provide city and state both", false);
		}
	}

}
