package com.youblog.services;

import java.io.IOException;
import java.util.Map;

import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.youblog.payloads.GetPostDetailsRequest;
import com.youblog.payloads.PostDetailsListRequest;
import com.youblog.payloads.UpdatePostDetailsRequest;

import jakarta.servlet.http.HttpServletResponse;

public interface PostDetailsService {

	public ResponseEntity<Map<String, Object>> postCreate(MultipartFile postMedia, String jsonData);

	public ResponseEntity<Map<String, Object>> postList(PostDetailsListRequest postDetailsListRequest);

	public ResponseEntity<GridFsResource> getMedia(String id);

	public void downloadMedia(String id,HttpServletResponse response)  throws IOException;

	public ResponseEntity<Map<String, Object>> postGet(GetPostDetailsRequest getPostDetailsRequest);

	public ResponseEntity<Map<String, Object>> postUpdate(UpdatePostDetailsRequest updatePostDetailsRequest);
	
}
