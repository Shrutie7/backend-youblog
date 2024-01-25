package com.youblog.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostDetailsListRequest {
	
	private Long userId;
	
	private Long categoryId;
	
	private Long roleId;
}
