package com.youblog.entities;

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
@Table(name = "WORKFLOW_MASTER")


public class WorkflowMasterEntity { 

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "WORKFLOW_MASTER_ID")
	private Long workflowMasterId;

	@Column(name = "WORKFLOW_NAME")
	private String workflowName;

	@Column(name = "INITIATE_ROLE_ID")
	private Long initiateRoleId;

	@Column(name = "ACTION_ROLE_ID")
	private Long actionRoleId;

	}