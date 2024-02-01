package com.youblog.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
import com.youblog.services.PostDetailsService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/post")
@CrossOrigin(origins = "*")
public class PostDetailsController {
	
	@Autowired
	private PostDetailsService postDetailsService;
	
	@PostMapping("/create")
	public ResponseEntity<Map<String, Object>> postCreate(@RequestPart("content") MultipartFile postMedia, @RequestPart("jsonData") String jsonData) {
		return postDetailsService.postCreate(postMedia,jsonData);
	}
	
	@PostMapping("/list")
	public ResponseEntity<Map<String, Object>> postList(@RequestBody PostDetailsListRequest postDetailsListRequest) {
		return postDetailsService.postList(postDetailsListRequest);
	}
	
	@GetMapping("/get/media/{id}")
	public ResponseEntity<GridFsResource> getMedia(@PathVariable String id, Model model) throws Exception {
	    ResponseEntity<GridFsResource> media = postDetailsService.getMedia(id);
	    return media;
	}
	
	@GetMapping("/download/media/{id}")
	public void downloadMedia(@PathVariable String id, HttpServletResponse response) throws Exception {
	    postDetailsService.downloadMedia(id,response);       
	}
	
	@PostMapping("/get")
	public ResponseEntity<Map<String, Object>> postGet(@RequestBody GetPostDetailsRequest getPostDetailsRequest) {
		return postDetailsService.postGet(getPostDetailsRequest);
	}
	
	@PostMapping("/update")
	public ResponseEntity<Map<String, Object>> postUpdate(@RequestBody UpdatePostDetailsRequest updatePostDetailsRequest) {
		return postDetailsService.postUpdate(updatePostDetailsRequest);
	}
	
	@PostMapping("/like")
	public ResponseEntity<Map<String, Object>> postLike(@RequestBody PostLikeRequest postLikeRequest) {
		return postDetailsService.postLike(postLikeRequest);
	}
	
	@PostMapping("/dislike")
	public ResponseEntity<Map<String, Object>> postDisLike(@RequestBody PostLikeRequest postLikeRequest) {
		return postDetailsService.postDisLike(postLikeRequest);
	}
	
	@PostMapping("/likes/list")
	public ResponseEntity<Map<String, Object>> postLikeList(@RequestBody PostLikeRequest postLikeRequest) {
		return postDetailsService.postLikeList(postLikeRequest);
	}
	
	@PostMapping("/add/bookmark")
	public ResponseEntity<Map<String, Object>> postAddBookmark(@RequestBody PostBookmarkRequest postBookmarkRequest) {
		return postDetailsService.postAddBookmark(postBookmarkRequest);
	}
	
	@PostMapping("/remove/bookmark")
	public ResponseEntity<Map<String, Object>> postRemoveBookmark(@RequestBody PostBookmarkRequest postBookmarkRequest) {
		return postDetailsService.postRemoveBookmark(postBookmarkRequest);
	}
	
	@PostMapping("/bookmarks/list")
	public ResponseEntity<Map<String, Object>> postBookmarksList(@RequestBody PostBookmarkRequest postBookmarkRequest) {
		return postDetailsService.postBookmarksList(postBookmarkRequest);
	} 
	
	@PostMapping("/comment/add")
	public ResponseEntity<Map<String, Object>> postCommentAdd(@RequestBody PostCommentAddRequest postCommentAddRequest) {
		return postDetailsService.postCommentAdd(postCommentAddRequest);
	}
	
	@PostMapping("/comment/edit")
	public ResponseEntity<Map<String, Object>> postCommentEdit(@RequestBody PostCommentEditRequest postCommentEditRequest) {
		return postDetailsService.postCommentEdit(postCommentEditRequest);
	}
	
	@PostMapping("/comment/list")
	public ResponseEntity<Map<String, Object>> postCommentList(@RequestBody PostCommentListRequest postCommentListRequest) {
		return postDetailsService.postCommentList(postCommentListRequest);
	}
	
	@PostMapping("/comment/reply/list")
	public ResponseEntity<Map<String, Object>> postCommentReplyList(@RequestBody PostCommentReplyListRequest postCommentReplyListRequest) {
		return postDetailsService.postCommentReplyList(postCommentReplyListRequest);
	}
	
	@PostMapping("/comment/reply")
	public ResponseEntity<Map<String, Object>> postCommentReply(@RequestBody PostCommentReplyRequest postCommentReplyRequest) {
		return postDetailsService.postCommentReply(postCommentReplyRequest);
	}
	
	@PostMapping("/comment/delete")
	public ResponseEntity<Map<String, Object>> postCommentDelete(@RequestBody PostCommentReplyListRequest postCommentDeleteRequest) {
		return postDetailsService.postCommentDelete(postCommentDeleteRequest);
	}
	
	@PostMapping("/list/user/based")
	public ResponseEntity<Map<String, Object>> postListBasedOnUser(@RequestBody PostDetailsListRequest postDetailsListRequest) {
		return postDetailsService.postListBasedOnUser(postDetailsListRequest);
	}
}
