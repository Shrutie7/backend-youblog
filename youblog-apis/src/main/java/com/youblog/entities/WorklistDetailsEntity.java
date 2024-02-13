package com.youblog.entities;

import java.util.Date;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "WORKLIST_DETAILS")
public class WorklistDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "WORKLIST_DETAILS_ID")
	private Long worklistDetailsId;

	@Column(name = "INITIATED_USER_ID")
	private Long initiatedUserId;

	@Column(name = "WORKFLOW_MASTER_ID")
	private Long workflowMasterId;

	@Column(name = "ACTION_USER_ID")
	private Long actionUserId;

	@Column(name = "INITIATED_DATA")
	@JdbcTypeCode(SqlTypes.JSON)
	private Map<String, Object> initiatedData;

	@Column(name = "INITIATED_DATE")
	private Date initiatedDate;

	@Column(name = "WORKLIST_STATUS")
	private String worklistStatus;

	@Column(name = "ACTED_DATE")
	private Date actedDate;

}
