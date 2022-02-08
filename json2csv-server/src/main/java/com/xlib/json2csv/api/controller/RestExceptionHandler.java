package com.xlib.json2csv.api.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.xlib.json2csv.domain.ApiResponse;
import com.xlib.json2csv.exceptions.JSON2CSVException;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author <a href="eap.eapereira@gmail.com">Edson Alves Pereira</a>
 *
 */
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(JSON2CSVException.class)
	protected ResponseEntity<Object> handleSystemException(Exception exception, Object body, HttpHeaders headers, HttpStatus status, WebRequest request)
									throws Exception {
		log.debug("BEGIN");
		ApiResponse apiResponse = new ApiResponse();
		ResponseEntity<Object> responseEntity = null;

		apiResponse.setStatus(status);
		apiResponse.setMessage(exception.getMessage());

		responseEntity = new ResponseEntity<Object>(apiResponse, status);

		log.debug("END");
		return responseEntity;
	}

}
