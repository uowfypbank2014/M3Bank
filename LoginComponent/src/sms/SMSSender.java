package sms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.Logger;

import database.DBManager;

/**
 * This class is responsible to send out OTP SMS.
 */
public class SMSSender {
	
	private static String smsTemplate;
	private static String username;
	private static String password;
	private static String serverUrl;
	private static boolean enabled;
	
	private static final Logger logger = Logger.getLogger("otp");
	
	static {
		try {
			Properties properties = new Properties();
			properties.load(DBManager.class.getClassLoader().getResourceAsStream("sms/bulksms.properties"));
			smsTemplate = properties.getProperty("otp.template");
			username = properties.getProperty("sms.server.username");
			password = properties.getProperty("sms.server.password");
			serverUrl = properties.getProperty("sms.server.url");
			enabled = Boolean.parseBoolean(properties.getProperty("sms.enabled"));
		} catch (IOException e) {
			logger.error("Error while configuring SMS sender.", e);
		}
	}
	
	/**
	 * 
	 * @param cellNumber
	 * @param otppw
	 * @return
	 */
	public static boolean sendSMS(String cellNumber, Integer otppw) {
		if (!enabled) {
			return true;
		}
		String message = smsTemplate.replace("$OTP$", otppw.toString());
		try {
			String data = "";
			data += "username=" + URLEncoder.encode(username, "ISO-8859-1");
			data += "&password=" + URLEncoder.encode(password, "ISO-8859-1");
			data += "&message=" + URLEncoder.encode(message, "ISO-8859-1");
			data += "&want_report=1";
			data += "&msisdn=" + cellNumber;
			
			URL url = new URL(serverUrl);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			logger.info("Sending otp to cell: " + cellNumber);
			wr.write(data);
			wr.flush();
			
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				logger.debug("Response: " + line);
			}
			wr.close();
			rd.close();
			
			logger.info("OTP sent to " + cellNumber);
			return true;
		}
		catch(Exception e) {
			logger.error("Error while sending OTP to " + cellNumber, e);
			return false;
		}
	}
	
	/**
	 * Generates a random 6 digit integer.
	 * @return
	 */
	public static Integer generateOTP() {
		if (enabled) {
			//Below formula will generate random 6 digit number between 100000 AND 999999 (both inclusive).
			return new Random().nextInt(899999) + 100000;
		}
		else {//If sms sending is disabled 123456 will be used as OTP
			return 123456;
		}
	}
}
