package com.utilities.noname.WakeUp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Config {

	private File config = new File("config.cfg");

	private static Config instance;

	public Config() {
		if (config.exists() == false)
			try {
				config.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public String getIP() {
		return get("ip");
	}

	public String getMAC() {
		return get("mac");
	}

	public void setIP(String ip) {
		set("ip", ip);
	}

	public void setMAC(String mac) {
		set("mac", mac);
	}

	public String get(String path) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(config));

			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith(path)) {
					reader.close();
					return line.split(": ")[1];
				}
			}

			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void set(String path, Object obj) {
		try {
			List<String> lines = new ArrayList<String>();

			BufferedReader reader = new BufferedReader(new FileReader(config));
			boolean alreadyIn = false;

			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith(path)) {
					alreadyIn = true;
					lines.add(path + ": " + obj);
				} else
					lines.add(line);
			}

			if (alreadyIn == false)
				lines.add(path + ": " + obj);

			reader.close();

			PrintWriter writer = new PrintWriter(new FileWriter(config, false));
			for (String l : lines) {
				writer.println(l);
			}

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Config getInstance() {
		if (instance == null)
			instance = new Config();
		return instance;
	}
}
