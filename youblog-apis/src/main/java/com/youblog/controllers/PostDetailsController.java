package com.youblog.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.youblog.payloads.GetPostDetailsRequest;
import com.youblog.payloads.PostDetailsListRequest;
import com.youblog.payloads.UpdatePostDetailsRequest;
import com.youblog.services.PostDetailsService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/post")
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
}
