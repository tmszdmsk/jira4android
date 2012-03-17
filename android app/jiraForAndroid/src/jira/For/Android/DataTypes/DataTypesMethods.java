package jira.For.Android.DataTypes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import jira.For.Android.DLog;

public class DataTypesMethods {

	private static final String datePattern = "yyyy-MM-ddHH:mm:ss";
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
	        datePattern);
	private static SimpleDateFormat simpleDateFormatGMT = setFormatGMT();

	private static SimpleDateFormat setFormatGMT() {
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf;
	}

	public static Date GMTStringToLocalDate(String dateAsString) {
		Date date = null;
		dateAsString = dateAsString.replace('T', ' ');
		try {
			date = simpleDateFormatGMT.parse(dateAsString);
		} catch (ParseException e) {
			DLog.e("Error, program was unable to parse string to date",
			        e.getMessage());
			e.printStackTrace();
		}

		return date;
	}

	public static Date stringToDate(String dateAsString) {
		Date date = null;
		dateAsString = dateAsString.replace('T', ' ');
		try {
			date = simpleDateFormat.parse(dateAsString);
		} catch (ParseException e) {
			DLog.e("Error, program was unable to parse string to date",
			        e.getMessage());
			e.printStackTrace();
		}
		return date;
	}

	public static String dateToString(Date dateAsDate) {
		return simpleDateFormat.format(dateAsDate);
	}

	public static String dateToGMTString(Date dateAsDate) {

		return simpleDateFormatGMT.format(dateAsDate);
	}
}
