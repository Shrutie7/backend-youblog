package com.youblog.services.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.youblog.entities.CategoryDetailsEntity;
import com.youblog.repositories.CategoryDetailsRepository;
import com.youblog.services.CategoryDetailsService;
import com.youblog.utils.ResponseHandler;

@Service
public class CategoryDetailsServiceImpl implements CategoryDetailsService{
	@Autowired
	CategoryDetailsRepository categorydetailsrepo;
	
	@Override
	public ResponseEntity<Map<String, Object>> getCategoryList() {
		Map<String, Object> data = new HashMap<>();
		ArrayList<CategoryDetailsEntity> getcategorylist = categorydetailsrepo.getCategoryList();
		ArrayList<Map<String,Object>> categorylist = new ArrayList<>();
		
		if(getcategorylist == null ) {
			   data.put("categoryDetailsList", new ArrayList<>());
			return ResponseHandler.response(data,"category details not found" , false);
		}
		else
			
			getcategorylist.forEach(ele->{
				Map<String, Object> catlist = new HashMap<>();
				catlist.put("categoryId", ele.getCategory_id());
				catlist.put("categoryName", ele.getCategory_title());
				
			categorylist.add(catlist);
			});
			   data.put("categoryDetailsList", categorylist);
			return ResponseHandler.response(data, "category list found", true);
		}
		
	}

	


