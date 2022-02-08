package com.xlib.json2csv.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xlib.json2csv.domain.UploadResponse;
import com.xlib.json2csv.service.StorageService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author <a href="eap.eapereira@gmail.com">Edson Alves Pereira</a>
 *
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class UploadController {

	@Autowired
	private StorageService storageService;

	@PostMapping("/upload-file")
	public ResponseEntity<UploadResponse> uploadFile(@RequestParam("file") MultipartFile multipartFile, RedirectAttributes redirectAttributes) {
		log.debug("BEGIN");
		UploadResponse uploadResponse = new UploadResponse();
		ResponseEntity<UploadResponse> responseEntity = null;
		HttpStatus status = null;

		try {
			log.info("Blah 2022");
			storageService.save(multipartFile);

			uploadResponse.setStatus(0);
			uploadResponse.setMessage("File stored on server's file system.");

			status = HttpStatus.OK;
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			uploadResponse.setStatus(1);
			uploadResponse.setMessage(e.getMessage());

			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		responseEntity = new ResponseEntity<UploadResponse>(uploadResponse, status);

		log.debug("END");
		return responseEntity;
	}

}
