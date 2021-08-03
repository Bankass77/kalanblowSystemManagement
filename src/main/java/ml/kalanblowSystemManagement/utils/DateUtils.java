package ml.kalanblowSystemManagement.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtils {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");

	/**
	 * @return
	 */

	public static LocalDateTime today() {

		return LocalDateTime.now();
	}

	/**
	 * @return to day's date as dd-MM-YYYY format
	 */

	public static String todayAtToString() {
		log.info("Today format date is:{}", sdf.format(today()));
		return sdf.format(today());

	}

	/**
	 * @param localDateTime
	 * @return
	 */
	public static String formattedDate(LocalDateTime localDateTime) {
		log.debug("localDatime is: {}", localDateTime);
		return localDateTime != null ? sdf.format(localDateTime) : todayAtToString();
	}

}