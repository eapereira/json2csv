package com.xlib.json2csv.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import com.xlib.json2csv.domain.LogResponse;
import com.xlib.json2csv.exceptions.JSON2CSVException;
import com.xlib.json2csv.service.StorageService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author <a href="eap.eapereira@gmail.com">Edson Alves Pereira</a>
 *
 */
@Controller
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class LogController {

	@Autowired
	private StorageService storageService;

	@GetMapping("/log")
	public ResponseEntity<LogResponse> loadLog() throws JSON2CSVException {
		log.debug("BEGIN");
		ResponseEntity<LogResponse> responseEntity = null;
		LogResponse logResponse = null;
		String msg = null;
		String csvFilename = null;

		try {
			logResponse = new LogResponse();
			logResponse.setStatus(HttpStatus.OK);
			logResponse.setMessage(msg);
			logResponse.setLog(csvFilename);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new JSON2CSVException(e);
		}

		responseEntity = new ResponseEntity<LogResponse>(logResponse, logResponse.getStatus());

		log.debug("END");
		return responseEntity;

	}

}
