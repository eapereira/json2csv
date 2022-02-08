package com.xlib.json2csv.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author <a href="eap.eapereira@gmail.com">Edson Alves Pereira</a>
 *
 */
@Setter
@Getter
public class ConverterResponse extends ApiResponse {

	private String json;
	private String csv;

	private String csvFilename;

	public ConverterResponse() {

	}
}
