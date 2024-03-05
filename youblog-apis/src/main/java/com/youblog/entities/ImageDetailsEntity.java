package com.youblog.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document(collection = "image_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ImageDetailsEntity {
	
	@Id
    private String id;
    
    private String title;
        
    private String image;
}
