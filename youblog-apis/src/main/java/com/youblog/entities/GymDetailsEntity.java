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

@Entity
@Table(name = "gym_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GymDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gym_id")
	private Long gymId;

	@Column(name = "gym_name")
	private String gymName;

	@Column(name = "location_id")
	private Long locationId;

	@Column(name = "owner_id")
	private Long ownerId;

	@Column(name = "gym_address")
	private String gymAddress;

	@Column(name = "contact")
	private String contact;
}
