package com.youblog.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentEditRequest {
	
	private Long commentId;
	
	private String commentDesc;
	
	private Long userId;
}
