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
	@Column(name="gym_id")
	private Long gym_id;
	
	@Column(name="gym_name")
	private String gym_name;
	
	@Column(name="location_id")
	private Long location_id;
	
	@Column(name="owner_id")
	private Long owner_id;
	
	@Column(name="gym_address")
	private String gym_address;

	@Column(name="contact")
	private String contact;
}
