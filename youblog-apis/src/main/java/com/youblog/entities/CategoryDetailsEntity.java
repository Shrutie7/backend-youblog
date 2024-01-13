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
@Table(name = "category_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryDetailsEntity {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name="category_id")
private Long category_id;

@Column(name="category_title")
private String category_title;
}
