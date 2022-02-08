package com.xlib.json2csv.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author <a href="eap.eapereira@gmail.com">Edson Alves Pereira</a>
 *
 */
@Getter
@Setter
public class UploadResponse {

	private Integer status;
	private String message;

	public UploadResponse() {

	}
}
