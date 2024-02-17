package com.youblog.payloads;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassMasterCreateRequest {
	
	private Set<ClassMasterCreate> classList;
}
