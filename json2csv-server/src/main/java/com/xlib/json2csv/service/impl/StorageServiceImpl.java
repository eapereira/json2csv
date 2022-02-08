package com.xlib.json2csv.service.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.xlib.json2csv.exceptions.JSON2CSVException;
import com.xlib.json2csv.service.StorageService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author <a href="eap.eapereira@gmail.com">Edson Alves Pereira</a>
 *
 */
@Service
@Slf4j
public class StorageServiceImpl implements StorageService {
	private static final String CSV_DIR = "csv";
	private static final String JSON_DIR = "json";

	private static final DateTimeFormatter DT_FILENAME = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	@Override
	public void save(MultipartFile multipartFile) throws JSON2CSVException {
		log.debug("BEGIN");
		File file = null;
		StringBuilder sb = null;

		try {
			sb = new StringBuilder();
			sb.append(getJSonDirectory());
			sb.append(File.separator);
			sb.append(multipartFile.getOriginalFilename());

			file = new File(sb.toString());

			if (file.exists()) {
				file.delete();
			}

			multipartFile.transferTo(file);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new JSON2CSVException(e);
		}

		log.debug("END");
	}

	private String readFile(File file) throws JSON2CSVException {
		log.debug("BEGIN");
		StringBuilder sb = null;
		FileInputStream fis = null;
		InputStreamReader reader = null;
		char[] data = null;

		try {
			if (file.exists()) {
				fis = new FileInputStream(file);
				reader = new InputStreamReader(fis);

				data = new char[1024];

				sb = new StringBuilder();

				while (reader.read(data) > 0) {
					sb.append(data);
				}
			} else {
				log.info("The file [{}] doesn't exist in this file system:");
				throw new FileNotFoundException(file.getName());
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new JSON2CSVException(e);
		} finally {
			close(fis);
		}

		log.debug("END");
		return sb.toString();
	}

	private String getHomeDirectory() throws JSON2CSVException {
		log.debug("BEGIN");
		StringBuilder sb = new StringBuilder();
		File dir = null;

		sb.append(System.getProperty("user.home"));
		sb.append(File.separator);
		sb.append(".json2csv-server");

		dir = new File(sb.toString());

		if (!dir.exists()) {
			dir.mkdirs();
		}

		log.debug("END");
		return sb.toString();
	}

	private String getJSonDirectory() throws JSON2CSVException {
		log.debug("BEGIN");
		StringBuilder sb = new StringBuilder();
		File dir = null;

		sb.append(getHomeDirectory());
		sb.append(File.separator);
		sb.append(JSON_DIR);
		sb.append(File.separator);

		dir = new File(sb.toString());

		if (!dir.exists()) {
			dir.mkdirs();
		}

		log.debug("END");
		return sb.toString();
	}

	private String getCsvDirectory() throws JSON2CSVException {
		log.debug("BEGIN");
		StringBuilder sb = new StringBuilder();
		File dir = null;

		sb.append(getHomeDirectory());
		sb.append(File.separator);
		sb.append(CSV_DIR);
		sb.append(File.separator);

		dir = new File(sb.toString());

		if (!dir.exists()) {
			dir.mkdirs();
		}

		log.debug("END");
		return sb.toString();
	}

	@Override
	public void writeToStream(String filename, OutputStream outputStream) throws JSON2CSVException {
		log.debug("BEGIN");
		byte[] data = null;
		StringBuilder sb = null;
		File file = null;

		try {
			sb = new StringBuilder();
			sb.append(getCsvDirectory());
			sb.append(File.separator);
			sb.append(filename);

			file = new File(sb.toString());

			data = readBytes(file);

			outputStream.write(data);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new JSON2CSVException(e);
		}

		log.debug("END");
	}

	private byte[] readBytes(File file) throws JSON2CSVException {
		log.debug("BEGIN");
		byte[] data = null;
		FileInputStream fileInputStream = null;
		BufferedInputStream bufferedInputStream = null;

		try {
			if (file != null && file.exists()) {
				fileInputStream = new FileInputStream(file);
				bufferedInputStream = new BufferedInputStream(fileInputStream);

				data = new byte[bufferedInputStream.available()];

				bufferedInputStream.read(data);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new JSON2CSVException(e);
		} finally {
			close(bufferedInputStream);
			close(fileInputStream);
		}

		log.debug("END");
		return data;
	}

	private void close(InputStream inputStream) throws JSON2CSVException {
		log.debug("BEGIN");

		try {
			if (inputStream != null) {
				inputStream.close();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new JSON2CSVException(e);
		}

		log.debug("END");
	}

	private void close(OutputStream outputStream) throws JSON2CSVException {
		log.debug("BEGIN");

		try {
			if (outputStream != null) {
				outputStream.close();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new JSON2CSVException(e);
		}

		log.debug("END");
	}

	@Override
	public Resource readFileAsResource(String filename) throws JSON2CSVException {
		log.debug("BEGIN");
		Resource resource = null;

		StringBuilder sb = null;
		Path path = null;

		try {
			sb = new StringBuilder();
			sb.append(getCsvDirectory());
			sb.append(File.separator);
			sb.append(filename);

			path = Paths.get(sb.toString());

			resource = new UrlResource(path.toUri());

			if (!resource.exists() || !resource.isReadable()) {
				log.info("Could not read the file [{}]", sb.toString());
				throw new JSON2CSVException("Could not read the file [%s]", sb.toString());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new JSON2CSVException(e);
		}

		log.debug("END");
		return resource;
	}

	@Override
	public String readJSonFile(String filename) throws JSON2CSVException {
		log.debug("BEGIN");
		StringBuilder sb = null;
		String json = null;
		File file = null;

		try {
			sb = new StringBuilder();
			sb.append(getJSonDirectory());
			sb.append(File.separator);
			sb.append(filename);

			file = new File(sb.toString());

			json = readFile(file);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new JSON2CSVException(e);
		}

		log.debug("END");
		return json;
	}

	@Override
	public String createCsvFile(String filename, String csv) throws JSON2CSVException {
		log.debug("BEGIN");
		String csvFilename = null;
		File file = null;
		StringBuilder sb = null;
		LocalDateTime dateTime = null;

		try {
			if (StringUtils.isBlank(filename)) {
				dateTime = LocalDateTime.now();
				sb = new StringBuilder();

				sb.append(dateTime.format(DT_FILENAME));
				sb.append(".csv");

				csvFilename = sb.toString();
			} else {
				csvFilename = filename.replace(".json", ".csv");
			}

			sb = new StringBuilder();
			sb.append(getCsvDirectory());
			sb.append(File.separator);
			sb.append(csvFilename);

			file = new File(sb.toString());

			if (file.exists()) {
				file.delete();
			}

			log.info("Writing CSV data to file [{}]:", csvFilename);

			writeFile(file, csv);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new JSON2CSVException(e);
		}

		log.debug("END");
		return csvFilename;
	}

	private void writeFile(File file, String csv) throws JSON2CSVException {
		log.debug("BEGIN");
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(file);

			fos.write(csv.getBytes());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new JSON2CSVException(e);
		} finally {
			close(fos);
		}

		log.debug("END");
	}
}
