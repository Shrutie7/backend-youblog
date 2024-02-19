package com.youblog.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.youblog.utils.ResponseHandler;

/*
 * @ContollerAdvice is annotation, is used to handle the exceptions globally
 */
@CrossOrigin(origins = "*", maxAge = 36000)
@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> requestHandlingNoHandlerFound() {
		return ResponseHandler.response(null, "Invalid path", false);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> handleValidExptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		String err_message = null;
		for (Entry<String, String> message : errors.entrySet()) {
			err_message = message.getValue();
		}
		return ResponseHandler.response(null, err_message, false);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Map<String, Object>> handleBadRequestException(HttpMessageNotReadableException ex) {
		return ResponseHandler.response(null, "Please Provide RequestBody", false);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Map<String, Object>> handleBadRequestException(HttpRequestMethodNotSupportedException ex) {
		Set<org.springframework.http.HttpMethod> methods = ex.getSupportedHttpMethods();
		HttpMethod supportedMethod = null;
		for (HttpMethod method : methods) {
			supportedMethod = method;
		}
		return ResponseHandler.response(null,
				supportedMethod + " is supported method" + " not " + ex.getMethod() + " method", false);
	}

	@ExceptionHandler(InternalServerError.class)
	public ResponseEntity<Map<String, Object>> internalException(InternalServerError ex) {
		return ResponseHandler.response(null, ex.getMessage(), false);
	}

	@ExceptionHandler(MissingRequestHeaderException.class)
	public ResponseEntity<Map<String, Object>> internalException(MissingRequestHeaderException ex) {
		return ResponseHandler.response(null, "Missing header :" + ex.getHeaderName(), false);
	}
	
	@ExceptionHandler(InvalidDataAccessResourceUsageException.class)
	public ResponseEntity<Map<String, Object>> internalException(InvalidDataAccessResourceUsageException ex) {
		return ResponseHandler.response(null,"Exception : "+  ex.getCause(), false);
	}
	
	@ExceptionHandler(ClassCastException.class)
	public ResponseEntity<Map<String, Object>> internalException(ClassCastException ex) {
		return ResponseHandler.response(null,"Exception : "+  ex.getCause(), false);
	}
}
