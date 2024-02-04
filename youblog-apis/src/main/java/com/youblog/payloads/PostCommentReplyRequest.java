package com.youblog.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostCommentReplyRequest {
	
	private String commentDesc;
	
	private Long parentCommentId;
	
	private Long userId;
}
