package com.cpqi.andes.configuration;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DateStringToCalendarDeserializer extends JsonDeserializer<Calendar> {

	@Override
	public Calendar deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		try {
			String formatted = p.getValueAsString();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(formatted));
			return calendar;
		} catch (ParseException e) {
			throw new IOException(e);
		}
	}

}