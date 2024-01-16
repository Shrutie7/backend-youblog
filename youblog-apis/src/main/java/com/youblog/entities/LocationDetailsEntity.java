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
@Table(name = "location_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LocationDetailsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="location_id")
	private Long location_id;

	@Column(name="location_name")
	private String location_name;
	
	@Column(name="state")
	private String state;
	
	@Column(name="city")
	private String city;
}
