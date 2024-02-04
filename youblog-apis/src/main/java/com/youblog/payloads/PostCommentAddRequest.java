package com.youblog.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentAddRequest {
	
	private String commentDesc;
	
	private Long postId;
	
	private Long userId;
	
}
