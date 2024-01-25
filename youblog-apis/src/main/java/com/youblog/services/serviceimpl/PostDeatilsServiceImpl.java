package com.youblog.services.serviceimpl;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.youblog.entities.PostDetailsEntity;
import com.youblog.entities.UserDetailsEntity;
import com.youblog.payloads.GetPostDetailsRequest;
import com.youblog.payloads.PostDetailsListRequest;
import com.youblog.payloads.UpdatePostDetailsRequest;
import com.youblog.repositories.PostDetailsRepository;
import com.youblog.repositories.UserDetailsRepository;
import com.youblog.services.PostDetailsService;
import com.youblog.utils.DateParser;
import com.youblog.utils.ResponseHandler;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class PostDeatilsServiceImpl implements PostDetailsService {

	@Autowired
	private PostDetailsRepository postDetailsRepository;

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Autowired
	private GridFsOperations gridFsOperations;

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Override
	public ResponseEntity<Map<String, Object>> postCreate(MultipartFile postMedia, String jsonData) {
		JSONObject request = null;
		try {
			request = new JSONObject(jsonData);
		} catch (Exception e) {
			return ResponseHandler.response(null, "please provide valid request", false);
		}
		BasicDBObject metaData = new BasicDBObject();
		metaData.put("title", request.get("title").toString());
		ObjectId id = null;
		try {
			id = gridFsTemplate.store(postMedia.getInputStream(),
					request.get("title").toString() + Date.from(Instant.now()), postMedia.getContentType(), metaData);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseHandler.response("Cause : " + e.getLocalizedMessage(), "Failed to Create Post.", false);
		}
		try {
			PostDetailsEntity postDetailsEntity = new PostDetailsEntity();
			postDetailsEntity.setCategoryId(Long.valueOf(request.get("categoryId").toString()));
			postDetailsEntity.setCreatedDate(Date.from(Instant.now()));
			postDetailsEntity.setDecription(request.get("description").toString());
			postDetailsEntity.setUserId(Long.valueOf(request.get("userId").toString()));
			postDetailsEntity.setActiveFlag(true);
			postDetailsEntity.setUpdatedDate(Date.from(Instant.now()));
			postDetailsEntity.setUpdatedUserId(Long.valueOf(request.get("userId").toString()));
			postDetailsEntity.setArchiveFlag(false);
			postDetailsEntity.setContent(id.toString());
			postDetailsEntity.setTitle(request.get("title").toString());
			postDetailsRepository.save(postDetailsEntity);
			return ResponseHandler.response(null, "Post Created Successfully", true);
		} catch (Exception e) {
			gridFsOperations.delete(Query.query(Criteria.where("_id").is(id)));
			return ResponseHandler.response("Cause : " + e.getLocalizedMessage(), "Failed to Create Post.", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> postList(PostDetailsListRequest postDetailsListRequest) {
		List<PostDetailsEntity> postDetailsList = new ArrayList<>();
		if (postDetailsListRequest.getRoleId() == 4 || postDetailsListRequest.getRoleId() == 3) {
			if (postDetailsListRequest.getCategoryId() != null) {
				postDetailsList = postDetailsRepository.postListWithCategory(postDetailsListRequest.getUserId(),
						postDetailsListRequest.getCategoryId());
			} else {
				postDetailsList = postDetailsRepository.postListWithUserId(postDetailsListRequest.getUserId());
			}
		} else if (postDetailsListRequest.getRoleId() == 2) {
			if (postDetailsListRequest.getCategoryId() != null) {
				postDetailsList = postDetailsRepository.postListWithCategoryForOwner(postDetailsListRequest.getUserId(),
						postDetailsListRequest.getCategoryId());
			} else {
				postDetailsList = postDetailsRepository.postListWithUserIdForOwner(postDetailsListRequest.getUserId());
			}
		}
		if (postDetailsList.size() > 0) {
			JSONObject response = new JSONObject();
			for (PostDetailsEntity data : postDetailsList) {
				JSONObject subResponse = new JSONObject();
				subResponse.put("postId", data.getPostId());
				subResponse.put("title", data.getTitle() != null ? data.getTitle() : "");
				subResponse.put("contentUrl", data.getContent() != null ? "/post/get/media/" + data.getContent() : "");
				subResponse.put("categoryId", data.getCategoryId() != null ? data.getCategoryId() : "");
				subResponse.put("postedDate",
						data.getCreatedDate() != null
								? DateParser.dateToString("dd MMM yy HH:mm", data.getCreatedDate())
								: "");
				subResponse.put("updatedDate",
						data.getUpdatedDate() != null
								? DateParser.dateToString("dd MMM yy HH:mm", data.getUpdatedDate())
								: "");
				subResponse.put("updatedUserId", data.getUpdatedUserId() != null ? data.getUpdatedUserId() : "");
				subResponse.put("remarks", data.getRemarks() != null ? data.getRemarks() : "");
				subResponse.put("description", data.getDecription() != null ? data.getDecription() : "");
				UserDetailsEntity userData = userDetailsRepository.updateUserDetails(data.getUserId());
				JSONObject userResponse = new JSONObject();
				if (userData != null) {
					userResponse.put("userId", userData.getUserId());
					userResponse.put("userName", userData.getFirstName() + " " + userData.getLastName());
					userResponse.put("roleId", userData.getRoleId());
					userResponse.put("emailId", userData.getEmailId());
					userResponse.put("locationId", userData.getLocationId());
				}
				subResponse.put("postedBy", userResponse);
				response.append("postList", subResponse);
			}
			return ResponseHandler.response(response.toMap(), "Post Details Fetched Successfully!", true);
		} else {
			return ResponseHandler.response(null, "No Posts Found.", false);
		}
	}

	@Override
	public ResponseEntity<GridFsResource> getMedia(String id) {
		GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(id)));
		GridFsResource resource = gridFsTemplate.getResource(file.getFilename());
		return ResponseEntity.ok().contentType(MediaType.valueOf(resource.getContentType())).body(resource);
	}

	@Override
	public void downloadMedia(String id, HttpServletResponse response) throws IOException {
		GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(id)));
		response.setContentType(file.getMetadata().getString("_contentType").split("/")[1]);
		response.setHeader("content-Disposition", "attachment; filename=" + file.getMetadata().getString("title") + "."
				+ file.getMetadata().getString("_contentType").split("/")[1]);
		InputStream stream = gridFsOperations.getResource(file).getInputStream();
		FileCopyUtils.copy(stream, response.getOutputStream());
	}

	@Override
	public ResponseEntity<Map<String, Object>> postGet(GetPostDetailsRequest getPostDetailsRequest) {
		if (getPostDetailsRequest.getPostId() == null) {
			return ResponseHandler.response(null, "Please Provide PostId", false);
		}
		Optional<PostDetailsEntity> postDetails = postDetailsRepository.findById(getPostDetailsRequest.getPostId());
		if (postDetails.isEmpty()) {
			return ResponseHandler.response(null, "Post Details Not Found", false);
		}
		PostDetailsEntity data = postDetails.get();
		JSONObject response = new JSONObject();
		response.put("postId", data.getPostId());
		response.put("title", data.getTitle() != null ? data.getTitle() : "");
		response.put("contentUrl", data.getContent() != null ? "/post/get/media/" + data.getContent() : "");
		response.put("categoryId", data.getCategoryId() != null ? data.getCategoryId() : "");
		response.put("postedDate",
				data.getCreatedDate() != null ? DateParser.dateToString("dd MMM yy HH:mm", data.getCreatedDate()) : "");
		response.put("updatedDate",
				data.getUpdatedDate() != null ? DateParser.dateToString("dd MMM yy HH:mm", data.getUpdatedDate()) : "");
		response.put("updatedUserId", data.getUpdatedUserId() != null ? data.getUpdatedUserId() : "");
		response.put("remarks", data.getRemarks() != null ? data.getRemarks() : "");
		response.put("description", data.getDecription() != null ? data.getDecription() : "");
		UserDetailsEntity userData = userDetailsRepository.updateUserDetails(data.getUserId());
		JSONObject userResponse = new JSONObject();
		if (userData != null) {
			userResponse.put("userId", userData.getUserId());
			userResponse.put("userName", userData.getFirstName() + " " + userData.getLastName());
			userResponse.put("roleId", userData.getRoleId());
			userResponse.put("emailId", userData.getEmailId());
			userResponse.put("locationId", userData.getLocationId());
		}
		response.put("postedBy", userResponse);
		response.put("activeFlag", data.getActiveFlag());
		response.put("archiveFlag", data.getArchiveFlag());
		return ResponseHandler.response(response.toMap(), "Post Details Fetched Successfully.", true);
	}

	@Override
	public ResponseEntity<Map<String, Object>> postUpdate(UpdatePostDetailsRequest updatePostDetailsRequest) {
		if (updatePostDetailsRequest.getPostId() == null) {
			return ResponseHandler.response(null, "Please Provide PostId", false);
		}
		Optional<PostDetailsEntity> postDetails = postDetailsRepository.findById(updatePostDetailsRequest.getPostId());
		if (postDetails.isEmpty()) {
			return ResponseHandler.response(null, "Post Details Not Found", false);
		}
		PostDetailsEntity postDetailsEntity = postDetails.get();
		postDetailsEntity.setTitle(updatePostDetailsRequest.getTitle());
		postDetailsEntity.setDecription(updatePostDetailsRequest.getDescription());
		postDetailsEntity.setUpdatedUserId(updatePostDetailsRequest.getUserId());
		if (updatePostDetailsRequest.getArchiveFlag() != null) {
			postDetailsEntity.setArchiveFlag(updatePostDetailsRequest.getArchiveFlag());
		}
		if (updatePostDetailsRequest.getActiveFlag() != null) {
			postDetailsEntity.setRemarks(updatePostDetailsRequest.getRemarks());
			postDetailsEntity.setActiveFlag(updatePostDetailsRequest.getActiveFlag());
			if (!updatePostDetailsRequest.getActiveFlag()) {
				gridFsOperations.delete(Query.query(Criteria.where("_id").is(postDetailsEntity.getContent())));
			}
		}
		postDetailsRepository.save(postDetailsEntity);
		return ResponseHandler.response(null, "Post Details Updated Successfully", true);
	}
}
