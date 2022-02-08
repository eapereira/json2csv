package com.xlib.json2csv.domain;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author <a href="eap.eapereira@gmail.com">Edson Alves Pereira</a>
 *
 */
@Getter
@Setter
public class ApiError {

	private HttpStatus status;
	private String message;

	public ApiError() {

	}
}
