package com.xlib.json2csv.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xlib.json2csv.exceptions.JSON2CSVException;
import com.xlib.json2csv.service.StorageService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author <a href="eap.eapereira@gmail.com">Edson Alves Pereira</a>
 *
 */
@CrossOrigin(origins = "http://localhost:4200")
@Controller
@Slf4j
public class DownloadController {

	private static final String CONTENT_TYPE_CSV = "application/csv";

	@Autowired
	private StorageService storageService;

	@GetMapping("/download-file")
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(@RequestParam String filename) throws JSON2CSVException {
		log.debug("BEGIN");
		Resource resource = null;
		ResponseEntity<Resource> responseEntity = null;
		String attachment = null;

		try {
			log.info("Searching and loading file [{}]", filename);
			resource = storageService.readFileAsResource(filename.replace(".json", ".csv"));

			log.info("Downloading file [{}]:", resource.getFilename());
			attachment = String.format("attachment; filename=\"%s\"", resource.getFilename());
			responseEntity = ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_CSV).header(HttpHeaders.CONTENT_DISPOSITION, attachment)
											.body(resource);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new JSON2CSVException(e);
		}

		log.debug("END");
		return responseEntity;
	}

}
