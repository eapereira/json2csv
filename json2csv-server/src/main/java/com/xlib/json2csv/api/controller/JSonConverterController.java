package com.xlib.json2csv.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xlib.json2csv.domain.ConverterResponse;
import com.xlib.json2csv.exceptions.JSON2CSVException;
import com.xlib.json2csv.service.JSonConverterService;
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
public class JSonConverterController {

	@Autowired
	private JSonConverterService converterService;

	@Autowired
	private StorageService storageService;

	@PostMapping("/converter-json")
	public ResponseEntity<ConverterResponse> convertTextToJSon(@RequestParam("jsonText") String json) throws JSON2CSVException {
		log.debug("BEGIN");
		ResponseEntity<ConverterResponse> responseEntity = null;
		ConverterResponse converterResponse = null;
		String csv = null;
		String msg = null;
		String csvFilename = null;

		try {
			csv = convertJSon(json);

			csvFilename = storageService.createCsvFile(null, csv);
			msg = String.format("The JSON was converted to CSV file[{}]:", csvFilename);

			converterResponse = new ConverterResponse();
			converterResponse.setStatus(HttpStatus.OK);
			converterResponse.setMessage(msg);
			converterResponse.setCsvFilename(csvFilename);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new JSON2CSVException(e);
		}

		responseEntity = new ResponseEntity<ConverterResponse>(converterResponse, converterResponse.getStatus());

		log.debug("END");
		return responseEntity;
	}

	@PostMapping("/converter-json-file")
	public ResponseEntity<ConverterResponse> convertFileToJSon(@RequestParam("jsonFilename") String jsonFilename) throws JSON2CSVException {
		log.debug("BEGIN");
		ResponseEntity<ConverterResponse> responseEntity = null;
		ConverterResponse converterResponse = null;
		String msg = null;
		String json = null;
		String csv = null;
		String csvFilename = null;

		try {
			log.info("Receiving JSON file [{}] to convert into CSV:", jsonFilename);

			json = storageService.readJSonFile(jsonFilename);
			csv = convertJSon(json);

			csvFilename = storageService.createCsvFile(jsonFilename, csv);

			msg = String.format("The JSON file [%s] was converted to CSV", jsonFilename);

			converterResponse = new ConverterResponse();
			converterResponse.setStatus(HttpStatus.OK);
			converterResponse.setMessage(msg);
			converterResponse.setCsvFilename(csvFilename);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new JSON2CSVException(e);
		}

		responseEntity = new ResponseEntity<ConverterResponse>(converterResponse, converterResponse.getStatus());

		log.debug("END");
		return responseEntity;
	}

	private String convertJSon(String json) throws JSON2CSVException {
		log.debug("BEGIN");
		String csv = null;

		try {
			log.info("Receiving JSON to convert into CSV:");
			log.info(json);

			csv = converterService.convertTextToCsv(json);
			log.info("CSV create from JSON:");
			log.info(csv);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new JSON2CSVException(e);
		}

		log.debug("END");
		return csv;
	}
}
