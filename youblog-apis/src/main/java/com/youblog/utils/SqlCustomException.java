package com.youblog.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@EqualsAndHashCode(callSuper = true)
public class SqlCustomException extends Exception {

	private static final long serialVersionUID = 1L;

	private String message;

}
