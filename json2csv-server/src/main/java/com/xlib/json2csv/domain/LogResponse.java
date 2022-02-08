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
public class LogResponse extends ApiResponse {

	private String log;

	public LogResponse() {

	}
}
