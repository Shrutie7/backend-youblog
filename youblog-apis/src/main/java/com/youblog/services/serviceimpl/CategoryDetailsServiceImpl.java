package com.youblog.services.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.youblog.entities.CategoryDetailsEntity;
import com.youblog.payloads.CategoryListRequest;
import com.youblog.repositories.CategoryDetailsRepository;
import com.youblog.services.CategoryDetailsService;
import com.youblog.utils.ResponseHandler;

@Service
public class CategoryDetailsServiceImpl implements CategoryDetailsService {
	@Autowired
	CategoryDetailsRepository categoryDetailsRepository;

	@Override
	public ResponseEntity<Map<String, Object>> categoryList() {
		Map<String, Object> data = new HashMap<>();
		List<CategoryDetailsEntity> categoryList = categoryDetailsRepository.getCategoryList();
		List<Map<String, Object>> categories = new ArrayList<>();

		if (categoryList == null) {
			data.put("categoryDetailsList", categories);
			return ResponseHandler.response(data, "category details not found", false);
		} else

			categoryList.forEach(ele -> {
				Map<String, Object> catlist = new HashMap<>();
				catlist.put("categoryId", ele.getCategoryId());
				catlist.put("categoryName", ele.getCategoryName());

				categories.add(catlist);
			});
		data.put("categoryDetailsList", categories);
		return ResponseHandler.response(data, "category list found", true);
	}

	@Override
	public ResponseEntity<Map<String, Object>> userCategoryList(CategoryListRequest categoryListRequest) {
		Map<String, Object> data = new HashMap<>();
		List<CategoryDetailsEntity> categoryList = categoryDetailsRepository
				.userCategoryList(categoryListRequest.getGymId());
		List<Map<String, Object>> categories = new ArrayList<>();

		if (categoryList == null) {
			data.put("categoryDetailsList", categories);
			return ResponseHandler.response(data, "category details not found", false);
		} else

			categoryList.forEach(ele -> {
				Map<String, Object> catlist = new HashMap<>();
				catlist.put("categoryId", ele.getCategoryId());
				catlist.put("categoryName", ele.getCategoryName());

				categories.add(catlist);
			});
		data.put("categoryDetailsList", categories);
		return ResponseHandler.response(data, "category list found", true);
	}

}
