package com.xlib.json2csv.service;

import com.xlib.json2csv.exceptions.JSON2CSVException;

/**
 * 
 * @author <a href="eap.eapereira@gmail.com">Edson Alves Pereira</a>
 *
 */
public interface JSonConverterService {

	String convertTextToCsv(String json) throws JSON2CSVException;

	boolean validate(String json) throws JSON2CSVException;

}
