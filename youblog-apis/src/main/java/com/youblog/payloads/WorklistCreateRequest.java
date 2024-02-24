package com.youblog.payloads;

import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WorklistCreateRequest {
	
	private Long initiatedUserId;
	
	private Long workflowMasterId;
	
	private Long actionUserId;
	
	@JdbcTypeCode(SqlTypes.JSON)
	private Map<String, Object> initiatedData;
	
}
