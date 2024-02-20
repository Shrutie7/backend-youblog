package com.youblog.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassDetailsUpdateRequest {

	private Long classDetailsId;

	private Long tempTimeId;

	private Boolean tempChangeFlag;

	private Boolean tempCancelFlag;

	private Boolean activeFlag;
}
