package com.appsdeveloper.app.ws.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = {UsernameNotFoundException.class})
	public ResponseEntity<Object> handleUsernameNotFoundException(
			UsernameNotFoundException ex, WebRequest request) {
		
		ExceptionResponse response = 
				new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = {RuntimeException.class})
	public ResponseEntity<Object> handleUsernameNotFoundException(
			RuntimeException ex, WebRequest request) {
		
		ExceptionResponse response = 
				new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
