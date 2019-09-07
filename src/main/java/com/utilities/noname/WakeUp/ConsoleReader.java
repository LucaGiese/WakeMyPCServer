package com.utilities.noname.WakeUp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleReader extends Logger {

	Thread readerThread;

	public ConsoleReader() {
		readerThread = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						if (line.equalsIgnoreCase("stop")) {
							System.exit(0);
						} else if (line.equalsIgnoreCase("restart")) {
							WakeOnLan.getInstance().stop();

							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							WakeOnLan.getInstance();
						} else {
							log("This Command doesn't exsist");
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		readerThread.start();
	}

}
