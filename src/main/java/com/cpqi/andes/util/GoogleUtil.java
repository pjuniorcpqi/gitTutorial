package com.cpqi.andes.util;

import java.io.DataOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GoogleUtil {

	private static final String URL_GOOGLE = "https://www.google.com/recaptcha/api/siteverify";
	private static final String GOOGLE_RECAPTCHA_SECRET_KEY = "6LfbLSATAAAAAK-5UC2uqp4rqWiFGmu_unBnuGYX";

	public static boolean verifyReCaptcha(HttpServletRequest req, String response) throws Exception {

		URL obj = new URL(URL_GOOGLE);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("user-agent", req.getHeader("user-agent"));
		con.setRequestProperty("accept-language", req.getHeader("accept-language"));
		con.setRequestProperty("cache-control", req.getHeader("cache-control"));

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());

		String postValue = buildPostGoogleReCaptchaResponse(req, response);

		wr.writeBytes(postValue);
		wr.flush();
		wr.close();

		@SuppressWarnings("rawtypes")
		Map myMap = new HashMap();
		ObjectMapper objectMapper = new ObjectMapper();
		myMap = objectMapper.readValue(con.getInputStream(), HashMap.class);
		con.disconnect();

		boolean success = (boolean) myMap.get("success");

		return success;

	}

	private static String buildPostGoogleReCaptchaResponse(HttpServletRequest req, String response) {
		StringBuilder jsonPostValue = new StringBuilder();

		jsonPostValue.append("secret=");
		jsonPostValue.append(GOOGLE_RECAPTCHA_SECRET_KEY);
		jsonPostValue.append("&response=");
		jsonPostValue.append(response);

		return jsonPostValue.toString();

	}

}
