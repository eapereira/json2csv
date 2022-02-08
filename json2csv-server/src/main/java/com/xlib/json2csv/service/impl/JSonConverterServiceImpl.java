package com.xlib.json2csv.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.xlib.json2csv.exceptions.JSON2CSVException;
import com.xlib.json2csv.service.JSonConverterService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author <a href="eap.eapereira@gmail.com">Edson Alves Pereira</a>
 *
 */
@Service
@Slf4j
public class JSonConverterServiceImpl implements JSonConverterService {
	private static Pattern REGEXP_JSON = Pattern.compile(
									"^(\\W+|)((\\W+|)(\\\"[a-zA-Z0-9]{0,})\\\"\\:)((\\W+|)\\[)(?=((\\W+|)\\{))(((\\W+|)(((\\\"[a-zA-Z0-9]{1,})\\\")\\:(\\W+|)((\\\".+)\\\"))(\\,|))+)(?=((\\W+|)\\}(\\,|)))(((\\W+|)\\])((\\W+|)\\}))");

	private static Pattern REGEXP_REGISTERS = Pattern.compile(
									"(?<=(\\{)(\\W+|).+(\\[)(\\W+|))((\\{)((\\W+|)(((\\\"[a-zA-Z0-9]{1,})\\\")\\:(\\W+|)((\\\".+)\\\"))(\\,|))+(?=((\\W+|)\\}(\\W+|)(\\,|))))");

	private static Pattern REGEXP_PAIRS = Pattern.compile("(((\\\")([a-zA-Z0-9]{1,})(\\\"))\\:(\\W|)((\\\")(.+)(\\\")))");

	private static final int JSON_GROUP_04 = 4;

	private static final int JSON_GROUP_09 = 9;

	private static final String CSV_DELIMITER = ",";

	private static final String LINE_BREAK = "\n";

	@Override
	public String convertTextToCsv(String json) throws JSON2CSVException {
		log.debug("BEGIN");
		String csv = null;

		try {
			if (validate(json)) {
				csv = convert(json);
			} else {
				throw new JSON2CSVException("Invalid JSON to convert");
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new JSON2CSVException(e);
		}

		log.debug("END");
		return csv;
	}

	private String convert(String json) throws JSON2CSVException {
		log.debug("BEGIN");
		String csv = null;
		Matcher matcher = null;
		String registers = null;
		List<Map<String, String>> list = null;

		try {
			matcher = REGEXP_REGISTERS.matcher(json);

			if (matcher.find()) {
				log.info("Starting to convert JSON text to CSV:");

				registers = matcher.group(0);

				log.info("Registers extracted from JSON:");
				log.info(registers);

				list = convertRegisters(registers);

				csv = createCsv(list);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new JSON2CSVException(e);
		}

		log.debug("END");
		return csv;
	}

	private String createCsv(List<Map<String, String>> list) throws JSON2CSVException {
		log.debug("BEGIN");
		String csv = null;
		StringBuilder sb = null;
		StringBuilder sbCsv = null;
		String line = null;
		Set<String> keys = null;
		Iterator<String> iterator = null;
		String key = null;
		String value = null;
		boolean firstLine = true;
		int keysTotal = 0;
		int currentKey = 0;

		try {
			sbCsv = new StringBuilder();

			for (Map<String, String> map : list) {
				keys = map.keySet();
				iterator = keys.iterator();
				keysTotal = keys.size();
				currentKey = 1;

				sb = new StringBuilder();

				while (iterator.hasNext()) {
					key = iterator.next();
					value = map.get(key);

					sb.append(value);

					if (currentKey < keysTotal) {
						sb.append(CSV_DELIMITER);
					}

					if (firstLine) {
						sbCsv.append(key);

						if (currentKey < keysTotal) {
							sbCsv.append(CSV_DELIMITER);
						}
					}

					currentKey++;
				}

				if (firstLine) {
					firstLine = false;
					sbCsv.append(LINE_BREAK);
				}

				sbCsv.append(sb.toString());
				sbCsv.append(LINE_BREAK);
			}

			csv = sbCsv.toString();

			log.info("Resulting CSV transformed:");
			log.info(csv);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new JSON2CSVException(e);
		}

		log.debug("END");
		return csv;
	}

	private List<Map<String, String>> convertRegisters(String registers) throws JSON2CSVException {
		log.debug("BEGIN");
		String register = null;
		Matcher matcherPairs = null;
		Map<String, String> map;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String name = null;
		String value = null;
		StringTokenizer stringTokenizer = null;

		stringTokenizer = new StringTokenizer(registers, "{");
		log.info("Extracting register from JSON:");
		log.info(registers);

		while (stringTokenizer.hasMoreTokens()) {
			register = stringTokenizer.nextToken();

			log.info("Processing the data from the register:");
			log.info(register);

			matcherPairs = REGEXP_PAIRS.matcher(register);

			map = new HashMap<String, String>();

			while (matcherPairs.find()) {
				name = matcherPairs.group(JSON_GROUP_04);
				value = matcherPairs.group(JSON_GROUP_09);

				log.info("Field with name[{}] and value[{}]:", name, value);
				map.put(name, value);
			}

			list.add(map);
		}

		log.debug("END");
		return list;
	}

	@Override
	public boolean validate(String json) throws JSON2CSVException {
		log.debug("BEGIN");
		Matcher matcher = null;

		try {
			if (StringUtils.isNotBlank(json)) {
				matcher = REGEXP_JSON.matcher(json);

				if (matcher.find()) {
					return true;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new JSON2CSVException(e);
		}

		log.debug("END");
		return false;
	}

}
