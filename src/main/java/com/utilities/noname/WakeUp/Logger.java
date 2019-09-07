package com.utilities.noname.WakeUp;

import java.util.Calendar;

public class Logger {

	public void log(String log) {
		Calendar c = Calendar.getInstance();
		String hour = c.get(Calendar.HOUR) + "";
		String min = c.get(Calendar.MINUTE) + "";
		String sec = c.get(Calendar.SECOND) + "";

		if (hour.length() < 2) {
			hour = "0" + hour;
		}

		if (min.length() < 2) {
			min = "0" + min;
		}

		if (sec.length() < 2) {
			sec = "0" + sec;
		}

		System.out.println(hour + ":" + min + ":" + sec + ": " + log);
	}

}
