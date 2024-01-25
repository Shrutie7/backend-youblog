package com.youblog.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostDetailsRequest {

	private Long postId;

	private String title;

	private String description;

	private Long userId;

	private Boolean activeFlag;

	private Boolean archiveFlag;

	private String remarks;
}
