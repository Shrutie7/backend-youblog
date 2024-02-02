package com.youblog.services.serviceimpl;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.json.JSONArray;
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
import com.youblog.entities.CommentDetailsEntity;
import com.youblog.entities.PostDetailsEntity;
import com.youblog.entities.PostLikesEntity;
import com.youblog.entities.PostSaveDetailsEntity;
import com.youblog.payloads.GetPostDetailsRequest;
import com.youblog.payloads.PostBookmarkRequest;
import com.youblog.payloads.PostCommentAddRequest;
import com.youblog.payloads.PostCommentEditRequest;
import com.youblog.payloads.PostCommentListRequest;
import com.youblog.payloads.PostCommentReplyListRequest;
import com.youblog.payloads.PostCommentReplyRequest;
import com.youblog.payloads.PostDetailsListRequest;
import com.youblog.payloads.PostLikeRequest;
import com.youblog.payloads.UpdatePostDetailsRequest;
import com.youblog.repositories.CommentDetailsRepository;
import com.youblog.repositories.PostDetailsRepository;
import com.youblog.repositories.PostLikesRepository;
import com.youblog.repositories.PostSaveDetailsRepository;
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
	private PostLikesRepository postLikesRepository;

	@Autowired
	private PostSaveDetailsRepository postSaveDetailsRepository;

	@Autowired
	private CommentDetailsRepository commentDetailsRepository;

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
			JSONArray json = (JSONArray) request.get("categoryId");
			Integer[] categories = new Integer[json.length()];
			int i = 0;
			for (Object data : json) {
				categories[i] = (Integer) data;
				i++;
			}
			postDetailsEntity.setCategoryId(categories);
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
				if (postDetailsListRequest.getBookmarkFlag()) {
					if (Boolean.valueOf(data[22].toString()) == postDetailsListRequest.getBookmarkFlag()) {
						response.append("postList", responseConstructor(data));
					}
				} else {
					response.append("postList", responseConstructor(data));
				}
			}
			if (response.length() != 0) {
				return ResponseHandler.response(response.toMap(), "Post Details Fetched Successfully!", true);
			} else {
				return ResponseHandler.response(null, "Post Details Not Found!", false);
			}
		} else {
			return ResponseHandler.response(null, "No Posts Found.", false);
		}
	}

	public Map<String, Object> responseConstructor(Object[] data) {
		JSONObject subResponse = new JSONObject();
		subResponse.put("postId", data[0] != null ? data[0] : "");
		subResponse.put("title", data[1] != null ? data[1].toString() : "");
		subResponse.put("contentUrl", data[2] != null ? "/post/get/media/" + data[2] : "");
		GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(data[2])));
		subResponse.put("contentType", file.getMetadata().getString("_contentType").split("/")[0]);
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
		subResponse.put("bookmarkStatus", data[22] != null ? data[22] : false);
		return subResponse.toMap();
	}

	@Override
	public ResponseEntity<GridFsResource> getMedia(String id) {
		GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(id)));
		GridFsResource resource = gridFsTemplate.getResource(file.getFilename());
		return ResponseEntity.ok().contentType(MediaType.valueOf(resource.getContentType())).body(resource);
	}

	@Override
	public void downloadMedia(String id, HttpServletResponse response)
			throws IOException {
		GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(id)));
		response.setContentType(file.getMetadata().getString("_contentType").split("/")[1]);
		response.setHeader("Content-Disposition", "attachment; filename=" + file.getMetadata().getString("title") + "."
				+ file.getMetadata().getString("_contentType").split("/")[1]);
		InputStream stream = gridFsOperations.getResource(file).getInputStream();
		FileCopyUtils.copy(stream, response.getOutputStream());
	}

	@Override
	public ResponseEntity<Map<String, Object>> postGet(GetPostDetailsRequest getPostDetailsRequest) {
		if (getPostDetailsRequest.getPostId() == null) {
			return ResponseHandler.response(null, "Please Provide PostId", false);
		}
		List<Object[]> postDetails = postDetailsRepository.getPostDetails(getPostDetailsRequest.getPostId(),
				getPostDetailsRequest.getUserId());
		if (postDetails.size() == 0) {
			return ResponseHandler.response(null, "Post Details Not Found", false);
		}
		Map<String, Object> subResponse = new HashMap<>();
		for (Object[] data : postDetails) {
			subResponse = responseConstructor(data);
		}
		return ResponseHandler.response(subResponse, "Post Details Fetched Successfully.", true);
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
		if (postLikes != null && postLikes.getActiveFlag()) {
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
		List<Object[]> postlLikesList = postLikesRepository.getPostLikesList(postLikeRequest.getPostId());
		if (postlLikesList.size() == 0) {
			return ResponseHandler.response(null, "Likes List Not Found", false);
		}
		JSONObject response = new JSONObject();
		postlLikesList.forEach(data -> {
			JSONObject subResponse = new JSONObject();
			subResponse.put("postId", data[0] != null ? data[0] : "");
			JSONObject userResponse = new JSONObject();
			if (data[6] != null) {
				userResponse.put("userId", data[6] != null ? data[6] : "");
				userResponse.put("userName", (data[2] != null && data[2] != " ") ? data[2].toString() : "");
				userResponse.put("roleId", data[3] != null ? data[3] : "");
				userResponse.put("emailId", data[4] != null ? data[4].toString() : "");
				userResponse.put("locationId", data[5] != null ? data[5] : "");
			}
			subResponse.put("likedUserData", userResponse);
			subResponse.put("likedOn", data[1] != null ? data[1].toString() : "");
			response.append("postLikesDetails", subResponse);
		});
		return ResponseHandler.response(response.toMap(), "Like Details Fetched Successfully", true);
	}

	@Override
	public ResponseEntity<Map<String, Object>> postAddBookmark(PostBookmarkRequest postBookmarkRequest) {
		if (postBookmarkRequest.getPostId() == null) {
			return ResponseHandler.response(null, "Please Provide Post Id", false);
		}
		PostSaveDetailsEntity postBookmark = postSaveDetailsRepository
				.getPostBookmarkUserId(postBookmarkRequest.getPostId(), postBookmarkRequest.getUserId());
		if (postBookmark != null) {
			postBookmark.setActiveFlag(true);
			postBookmark.setPostSavedDate(Date.from(Instant.now()));
			postSaveDetailsRepository.save(postBookmark);
		} else {
			PostSaveDetailsEntity bookmark = new PostSaveDetailsEntity();
			bookmark.setActiveFlag(true);
			bookmark.setPostSavedDate(Date.from(Instant.now()));
			bookmark.setUserId(postBookmarkRequest.getUserId());
			bookmark.setPostId(postBookmarkRequest.getPostId());
			postSaveDetailsRepository.save(bookmark);
		}
		return ResponseHandler.response(null, "Bookmarked Successfully", true);
	}

	@Override
	public ResponseEntity<Map<String, Object>> postRemoveBookmark(PostBookmarkRequest postBookmarkRequest) {
		if (postBookmarkRequest.getPostId() == null) {
			return ResponseHandler.response(null, "Please Provide Post Id", false);
		}
		PostSaveDetailsEntity postBookmark = postSaveDetailsRepository
				.getPostBookmarkUserId(postBookmarkRequest.getPostId(), postBookmarkRequest.getUserId());
		if (postBookmark != null && postBookmark.getActiveFlag()) {
			postBookmark.setActiveFlag(false);
			postBookmark.setPostSavedDate(Date.from(Instant.now()));
			postSaveDetailsRepository.save(postBookmark);
			return ResponseHandler.response(null, "Bookmark Removed Successfully", true);
		} else {
			return ResponseHandler.response(null, "Post Bookmark Details Not Found", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> postBookmarksList(PostBookmarkRequest postBookmarkRequest) {
		if (postBookmarkRequest.getUserId() == null) {
			return ResponseHandler.response(null, "Please Provide User Id", false);
		}
		List<PostSaveDetailsEntity> postBookmarks = postSaveDetailsRepository
				.getBookmarksList(postBookmarkRequest.getUserId());
		if (postBookmarks.size() == 0) {
			return ResponseHandler.response(null, "Bookmarks List Not Found", false);
		}
		JSONObject response = new JSONObject();
		postBookmarks.forEach(data -> {
			JSONObject subResponse = new JSONObject();
			subResponse.put("postId", data.getPostId());
			subResponse.put("bookmarkedOn",
					data.getPostSavedDate() != null
							? DateParser.dateToString("dd MMM YY HH:mm", data.getPostSavedDate())
							: "");
			response.append("bookmarksList", subResponse);
		});
		return ResponseHandler.response(response.toMap(), "Bookmark Details Fetched Successfully", true);
	}

	@Override
	public ResponseEntity<Map<String, Object>> postCommentAdd(PostCommentAddRequest postCommentAddRequest) {
		if (postCommentAddRequest.getCommentDesc() == null || postCommentAddRequest.getPostId() == null
				|| postCommentAddRequest.getUserId() == null) {
			return ResponseHandler.response(null, "Please Provide postId/ userId / comment Details", false);
		}
		CommentDetailsEntity commentDetailsEntity = new CommentDetailsEntity();
		commentDetailsEntity.setActiveFlag(true);
		commentDetailsEntity.setCommentDesc(postCommentAddRequest.getCommentDesc());
		commentDetailsEntity.setCommentedDate(Date.from(Instant.now()));
		commentDetailsEntity.setPostId(postCommentAddRequest.getPostId());
		commentDetailsEntity.setUserId(postCommentAddRequest.getUserId());
		try {
			commentDetailsRepository.save(commentDetailsEntity);
			return ResponseHandler.response(null, "Commented Successfully", true);
		} catch (Exception e) {
			return ResponseHandler.response(null, "Failed to Add Comment", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> postCommentEdit(PostCommentEditRequest postCommentEditRequest) {
		if (postCommentEditRequest.getCommentId() == null) {
			return ResponseHandler.response(null, "Please Provide commentId", false);
		}
		CommentDetailsEntity comment = commentDetailsRepository
				.getCommentDetails(postCommentEditRequest.getCommentId());
		if (comment == null) {
			return ResponseHandler.response(null, "Comment Details Not Found", false);
		}
		comment.setCommentDesc(postCommentEditRequest.getCommentDesc());
		commentDetailsRepository.save(comment);
		return ResponseHandler.response(null, "Comment Edited Successfully", true);
	}

	@Override
	public ResponseEntity<Map<String, Object>> postCommentList(PostCommentListRequest postCommentListRequest) {
		if (postCommentListRequest.getPostId() == null) {
			return ResponseHandler.response(null, "Please Provide PostId", false);
		}
		List<Object[]> commentDetails = commentDetailsRepository.postCommentList(postCommentListRequest.getPostId());
		if (commentDetails.size() == 0) {
			return ResponseHandler.response(null, "Comments List Not Found", false);
		}
		JSONObject response = new JSONObject();
		commentDetails.forEach(data -> {
			JSONObject subResponse = new JSONObject();
			subResponse.put("commentId", data[0] != null ? data[0] : "");
			JSONObject userResponse = new JSONObject();
			if (data[6] != null) {
				userResponse.put("userId", data[7] != null ? data[7] : "");
				userResponse.put("userName", (data[3] != null && data[3] != " ") ? data[3].toString() : "");
				userResponse.put("roleId", data[4] != null ? data[4] : "");
				userResponse.put("emailId", data[5] != null ? data[5].toString() : "");
				userResponse.put("locationId", data[6] != null ? data[6] : "");
			}
			subResponse.put("commentedUserData", userResponse);
			subResponse.put("comment", data[1] != null ? data[1].toString() : "");
			subResponse.put("commentedOn", data[2] != null ? data[2].toString() : "");
			response.append("commentList", subResponse);
		});
		return ResponseHandler.response(response.toMap(), "Comment Details Fetched Successfully", true);
	}

	@Override
	public ResponseEntity<Map<String, Object>> postCommentReplyList(
			PostCommentReplyListRequest postCommentReplyListRequest) {
		if (postCommentReplyListRequest.getCommentId() == null) {
			return ResponseHandler.response(null, "Please Provide Comment Id", false);
		}
		List<Object[]> postCommentList = commentDetailsRepository.getPostCommentReplyList(postCommentReplyListRequest.getCommentId());
		if (postCommentList.size() == 0) {
			return ResponseHandler.response(null, "Comment Reply List Not Found", false);
		}
		JSONObject response = new JSONObject();
		postCommentList.forEach(data -> {
			JSONObject subResponse = new JSONObject();
			subResponse.put("commentId", data[0] != null ? data[0] : "");
			JSONObject userResponse = new JSONObject();
			if (data[6] != null) {
				userResponse.put("userId", data[10] != null ? data[10] : "");
				userResponse.put("userName", (data[6] != null && data[6] != " ") ? data[6].toString() : "");
				userResponse.put("roleId", data[7] != null ? data[7] : "");
				userResponse.put("emailId", data[8] != null ? data[8].toString() : "");
				userResponse.put("locationId", data[9] != null ? data[9] : "");
			}
			subResponse.put("commentedUserData", userResponse);
			subResponse.put("comment", data[1] != null ? data[1].toString() : "");
			subResponse.put("parentCommentId", data[3]!=null?data[3]:"");
			subResponse.put("commentedOn", data[5] != null ? data[5].toString() : "");
			response.append("commentList", subResponse);
			response.append("postLikesDetails", subResponse);
		});
		return ResponseHandler.response(response.toMap(), "Like Details Fetched Successfully", true);
	}

	@Override
	public ResponseEntity<Map<String, Object>> postCommentReply(PostCommentReplyRequest postCommentReplyRequest) {
		if (postCommentReplyRequest.getCommentDesc() == null || postCommentReplyRequest.getParentCommentId() == null
				|| postCommentReplyRequest.getUserId() == null) {
			return ResponseHandler.response(null, "Please Provide parent commentId/ userId / comment Details", false);
		}
		CommentDetailsEntity commentDetailsEntity = new CommentDetailsEntity();
		commentDetailsEntity.setActiveFlag(true);
		commentDetailsEntity.setCommentDesc(postCommentReplyRequest.getCommentDesc());
		commentDetailsEntity.setCommentedDate(Date.from(Instant.now()));
		commentDetailsEntity.setCommentParentId(postCommentReplyRequest.getParentCommentId());
		commentDetailsEntity.setUserId(postCommentReplyRequest.getUserId());
		try {
			commentDetailsRepository.save(commentDetailsEntity);
			return ResponseHandler.response(null, "Comment Replied Successfully", true);
		} catch (Exception e) {
			return ResponseHandler.response(null, "Failed to Reply for Comment", false);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> postCommentDelete(PostCommentReplyListRequest postCommentDeleteRequest) {
		if(postCommentDeleteRequest.getCommentId()==null) {
			return ResponseHandler.response(null, "Please Provide Comment Id", false);
		}
		CommentDetailsEntity comment = commentDetailsRepository
				.getCommentDetails(postCommentDeleteRequest.getCommentId());
		if (comment == null) {
			return ResponseHandler.response(null, "Comment Details Not Found", false);
		}
		comment.setActiveFlag(false);
		commentDetailsRepository.save(comment);
		return ResponseHandler.response(null, "Comment Deleted Successfully", true);
	}

	@Override
	public ResponseEntity<Map<String, Object>> postListBasedOnUser(PostDetailsListRequest postDetailsListRequest) {
		if (postDetailsListRequest.getUserId() == null) {
			return ResponseHandler.response(null, "Please Provide User Id", false);
		}
		boolean archiveFlag = false;
		if (postDetailsListRequest.getArchiveFlag() != null) {
			archiveFlag = postDetailsListRequest.getArchiveFlag();
		}
		List<Object[]> postDetails = postDetailsRepository.postListBasedOnUser(postDetailsListRequest.getUserId(),
				archiveFlag);
		if (postDetails.size() > 0) {
			JSONObject response = new JSONObject();
			for (Object[] data : postDetails) {
				response.append("postList", responseConstructor(data));
			}
			return ResponseHandler.response(response.toMap(), "Post Details Fetched Successfully!", true);
		} else {
			return ResponseHandler.response(null, "No Posts Found.", false);
		}
	}
}
