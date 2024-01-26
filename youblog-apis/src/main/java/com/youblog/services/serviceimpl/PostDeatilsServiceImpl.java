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
import com.youblog.entities.PostLikesEntity;
import com.youblog.entities.UserDetailsEntity;
import com.youblog.payloads.GetPostDetailsRequest;
import com.youblog.payloads.PostDetailsListRequest;
import com.youblog.payloads.PostLikeRequest;
import com.youblog.payloads.UpdatePostDetailsRequest;
import com.youblog.repositories.PostDetailsRepository;
import com.youblog.repositories.PostLikesRepository;
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

	@Autowired
	private PostLikesRepository postLikesRepository;

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
		List<Object[]> postDetailsList = new ArrayList<>();
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
			for (Object[] data : postDetailsList) {
				JSONObject subResponse = new JSONObject();
				subResponse.put("postId", data[0] != null ? data[0] : "");
				subResponse.put("title", data[1] != null ? data[1].toString() : "");
				subResponse.put("contentUrl", data[2] != null ? "/post/get/media/" + data[2] : "");
				subResponse.put("categoryId", data[4] != null ? data[4] : "");
				subResponse.put("postedDate", data[5] != null ? data[5].toString() : "");
				subResponse.put("updatedDate", data[10] != null ? data[10].toString() : "");
				subResponse.put("remarks", data[9] != null ? data[9].toString() : "");
				subResponse.put("description", data[6] != null ? data[6] : "");
				subResponse.put("activeFlag", data[7] != null ? data[7] : true);
				subResponse.put("archiveFlag", data[8] != null ? data[8] : false);
				JSONObject createdUserResponse = new JSONObject();
				if (data[3] != null) {
					createdUserResponse.put("userId", data[3] != null ? data[3] : "");
					createdUserResponse.put("userName",
							(data[13] != null && data[13] != " ") ? data[13].toString() : "");
					createdUserResponse.put("roleId", data[14] != null ? data[14] : "");
					createdUserResponse.put("emailId", data[12] != null ? data[12].toString() : "");
					createdUserResponse.put("locationId", data[15] != null ? data[15] : "");
				}
				subResponse.put("postedBy", createdUserResponse);
				JSONObject updatedUserResponse = new JSONObject();
				if (data[11] != null) {
					updatedUserResponse.put("userId", data[11] != null ? data[11] : "");
					updatedUserResponse.put("userName",
							(data[17] != null && data[17] != " ") ? data[17].toString() : "");
					updatedUserResponse.put("roleId", data[18] != null ? data[18] : "");
					updatedUserResponse.put("emailId", data[16] != null ? data[16].toString() : "");
					updatedUserResponse.put("locationId", data[19] != null ? data[19] : "");
				}
				subResponse.put("updatedBy", updatedUserResponse);
				subResponse.put("likesCount", data[20] != null ? data[20] : 0);
				subResponse.put("likeStatus", data[21] != null ? data[21] : false);
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
		List<Object[]> postDetails = postDetailsRepository.getPostDetails(getPostDetailsRequest.getPostId(),getPostDetailsRequest.getUserId());
		if (postDetails.size() == 0) {
			return ResponseHandler.response(null, "Post Details Not Found", false);
		}
		JSONObject subResponse = new JSONObject();
		postDetails.forEach(data -> {
			subResponse.put("postId", data[0] != null ? data[0] : "");
			subResponse.put("title", data[1] != null ? data[1].toString() : "");
			subResponse.put("contentUrl", data[2] != null ? "/post/get/media/" + data[2] : "");
			subResponse.put("categoryId", data[4] != null ? data[4] : "");
			subResponse.put("postedDate", data[5] != null ? data[5].toString() : "");
			subResponse.put("updatedDate", data[10] != null ? data[10].toString() : "");
			subResponse.put("remarks", data[9] != null ? data[9].toString() : "");
			subResponse.put("description", data[6] != null ? data[6] : "");
			subResponse.put("activeFlag", data[7] != null ? data[7] : true);
			subResponse.put("archiveFlag", data[8] != null ? data[8] : false);
			JSONObject createdUserResponse = new JSONObject();
			if (data[3] != null) {
				createdUserResponse.put("userId", data[3] != null ? data[3] : "");
				createdUserResponse.put("userName", (data[13] != null && data[13] != " ") ? data[13].toString() : "");
				createdUserResponse.put("roleId", data[14] != null ? data[14] : "");
				createdUserResponse.put("emailId", data[12] != null ? data[12].toString() : "");
				createdUserResponse.put("locationId", data[15] != null ? data[15] : "");
			}
			subResponse.put("postedBy", createdUserResponse);
			JSONObject updatedUserResponse = new JSONObject();
			if (data[11] != null) {
				updatedUserResponse.put("userId", data[11] != null ? data[11] : "");
				updatedUserResponse.put("userName", (data[17] != null && data[17] != " ") ? data[17].toString() : "");
				updatedUserResponse.put("roleId", data[18] != null ? data[18] : "");
				updatedUserResponse.put("emailId", data[16] != null ? data[16].toString() : "");
				updatedUserResponse.put("locationId", data[19] != null ? data[19] : "");
			}
			subResponse.put("updatedBy", updatedUserResponse);
			subResponse.put("likesCount", data[20] != null ? data[20] : 0);
			subResponse.put("likeStatus", data[21] != null ? data[21] : false);
		});

//		JSONObject response = new JSONObject();
//		response.put("postId", data.getPostId());
//		response.put("title", data.getTitle() != null ? data.getTitle() : "");
//		response.put("contentUrl", data.getContent() != null ? "/post/get/media/" + data.getContent() : "");
//		response.put("categoryId", data.getCategoryId() != null ? data.getCategoryId() : "");
//		response.put("postedDate",
//				data.getCreatedDate() != null ? DateParser.dateToString("dd MMM yy HH:mm", data.getCreatedDate()) : "");
//		response.put("updatedDate",
//				data.getUpdatedDate() != null ? DateParser.dateToString("dd MMM yy HH:mm", data.getUpdatedDate()) : "");
//		response.put("updatedUserId", data.getUpdatedUserId() != null ? data.getUpdatedUserId() : "");
//		response.put("remarks", data.getRemarks() != null ? data.getRemarks() : "");
//		response.put("description", data.getDecription() != null ? data.getDecription() : "");
//		UserDetailsEntity userData = userDetailsRepository.getUserDetails(data.getUserId());
//		JSONObject userResponse = new JSONObject();
//		if (userData != null) {
//			userResponse.put("userId", userData.getUserId());
//			userResponse.put("userName", userData.getFirstName() + " " + userData.getLastName());
//			userResponse.put("roleId", userData.getRoleId());
//			userResponse.put("emailId", userData.getEmailId());
//			userResponse.put("locationId", userData.getLocationId());
//		}
//		response.put("postedBy", userResponse);
//		response.put("activeFlag", data.getActiveFlag());
//		response.put("archiveFlag", data.getArchiveFlag());
		return ResponseHandler.response(subResponse.toMap(), "Post Details Fetched Successfully.", true);
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

	@Override
	public ResponseEntity<Map<String, Object>> postLike(PostLikeRequest postLikeRequest) {
		if (postLikeRequest.getPostId() == null) {
			return ResponseHandler.response(null, "Please Provide Post Id", false);
		}
		PostLikesEntity postLikes = postLikesRepository.getPostLikeBasedOnUserId(postLikeRequest.getPostId(),
				postLikeRequest.getUserId());
		if (postLikes != null) {
			postLikes.setActiveFlag(true);
			postLikes.setLikedOnDate(Date.from(Instant.now()));
			postLikesRepository.save(postLikes);
		} else {
			PostLikesEntity likes = new PostLikesEntity();
			likes.setActiveFlag(true);
			likes.setLikedOnDate(Date.from(Instant.now()));
			likes.setLikedUserId(postLikeRequest.getUserId());
			likes.setPostId(postLikeRequest.getPostId());
			postLikesRepository.save(likes);
		}
		return ResponseHandler.response(null, "Liked Successfully", true);
	}

	@Override
	public ResponseEntity<Map<String, Object>> postDisLike(PostLikeRequest postLikeRequest) {
		if (postLikeRequest.getPostId() == null) {
			return ResponseHandler.response(null, "Please Provide Post Id", false);
		}
		PostLikesEntity postLikes = postLikesRepository.getPostLikeBasedOnUserId(postLikeRequest.getPostId(),
				postLikeRequest.getUserId());
		if (postLikes != null) {
			postLikes.setActiveFlag(false);
			postLikes.setLikedOnDate(Date.from(Instant.now()));
			postLikesRepository.save(postLikes);
			return ResponseHandler.response(null, "Disliked Successfully", true);
		} else {
			return ResponseHandler.response(null, "Post Like Details Not Found", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> postLikeList(PostLikeRequest postLikeRequest) {
		if (postLikeRequest.getPostId() == null) {
			return ResponseHandler.response(null, "Please Provide Post Id", false);
		}
		List<PostLikesEntity> postlLikesList = postLikesRepository.getPostLikesList(postLikeRequest.getPostId());
		if (postlLikesList.size() == 0) {
			return ResponseHandler.response(null, "Likes List Not Found", false);
		}
		JSONObject response = new JSONObject();
		postlLikesList.forEach(data -> {
			JSONObject subResponse = new JSONObject();
			subResponse.put("postId", data.getPostId());
			UserDetailsEntity userData = userDetailsRepository.getUserDetails(data.getLikedUserId());
			JSONObject userResponse = new JSONObject();
			if (userData != null) {
				userResponse.put("userId", userData.getUserId());
				userResponse.put("userName", userData.getFirstName() + " " + userData.getLastName());
				userResponse.put("roleId", userData.getRoleId());
				userResponse.put("emailId", userData.getEmailId());
				userResponse.put("locationId", userData.getLocationId());
			}
			subResponse.put("likedUserData", userResponse);
			subResponse.put("likedOn",
					data.getLikedOnDate() != null ? DateParser.dateToString("dd Mon YY HH:mm", data.getLikedOnDate())
							: "");
			response.append("postLikesDetails", subResponse);
		});
		return ResponseHandler.response(response.toMap(), "Like Details Fetched Successfully", true);
	}
}
