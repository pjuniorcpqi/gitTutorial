package com.cpqi.andes.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * <p>
 * DatesUtil
 * </p>
 *
 * @author Pedro Marcos <pjunior@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
public class DatesUtil {
    
    public static final String yyyyMMDDFormat = "yyyy-MM-dd";
    public static final String ddMMyyyyFormat = "dd-MM-yyyy";
    
    /**
     * Converts a string in format 'yyyyMMdd' to calendar.
     *
     * @param dateToParse
     *            ,
     *
     *
     * @return calendar
     */
    public static Calendar stringToCalendar(String dateToParse) {
	return stringToCalendar(dateToParse, yyyyMMDDFormat);
    }
    
    public static Calendar stringToCalendar(String dateToParse, String dateFormat) {
	DateFormat yyyyMMddDateFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
	Date dateCalendar = null;
	try {
	    dateCalendar = yyyyMMddDateFormat.parse(dateToParse);
	}
	catch (ParseException e) {
	    throw new IllegalArgumentException(dateToParse + " not recognized as a date in the " + dateFormat + " format.");
	}

	Calendar c = Calendar.getInstance();
	c.setTime(dateCalendar);
	return c;
    }
    
    public static String calendarToString(Calendar dateToParse) {

	SimpleDateFormat dateFormat = new SimpleDateFormat(ddMMyyyyFormat);
	
	return dateFormat.format(dateToParse.getTime());
	
    }

}
