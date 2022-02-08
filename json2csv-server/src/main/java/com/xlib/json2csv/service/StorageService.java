package com.xlib.json2csv.service;

import java.io.OutputStream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.xlib.json2csv.exceptions.JSON2CSVException;

/**
 * 
 * @author <a href="eap.eapereira@gmail.com">Edson Alves Pereira</a>
 *
 */
public interface StorageService {

	void save(MultipartFile multipartFile) throws JSON2CSVException;

	void writeToStream(String filename, OutputStream outputStream) throws JSON2CSVException;

	Resource readFileAsResource(String filename) throws JSON2CSVException;

	String readJSonFile(String filename) throws JSON2CSVException;

	String createCsvFile(String filename, String csv) throws JSON2CSVException;
}
