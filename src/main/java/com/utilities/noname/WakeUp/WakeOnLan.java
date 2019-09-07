package com.utilities.noname.WakeUp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;

import com.utilities.noname.WakeUp.client.ClientManager;

public class WakeOnLan extends Logger {

	private ServerSocket serverSocket;

	private static WakeOnLan instance;

	private boolean running = true;

	private static String ip = null, mac = null;

	public static void main(String[] args) {
		Config.getInstance();

		if (Config.getInstance().getIP() == null || Config.getInstance().getMAC() == null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("ENTER YOUR IP: ");
			try {
				ip = reader.readLine();

				while (ip.length() != 15) {
					System.out.print("PLEASE ENTER A VALID IP: ");
					ip = reader.readLine();
				}

				System.out.print("ENTER YOUR MAC: ");
				mac = reader.readLine();

				while (mac.length() != 17) {
					System.out.print("PLEASE ENTER A VALID MAC: ");
					mac = reader.readLine();
				}

				Config.getInstance().setIP(ip);
				Config.getInstance().setMAC(mac);
				reader.close();

				main(args);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			instance = new WakeOnLan();
			new ConsoleReader();
		}
	}

	public WakeOnLan() {
		try {
			mac = Config.getInstance().getMAC();
			ip = Config.getInstance().getIP();
			
			serverSocket = new ServerSocket(33599);

			ClientManager.getInstance();
			Acceptor.getInstance().start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			Acceptor.getInstance().stop();
			Thread.sleep(100);
			serverSocket.close();

			instance = null;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void wakeUp() {
		byte[] macBytes = getMacBytes(mac);
		byte[] bytes = new byte[6 + 16 * macBytes.length];
		for (int i = 0; i < 6; i++) {
			bytes[i] = (byte) 0xff;
		}
		for (int i = 6; i < bytes.length; i += macBytes.length) {
			System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
		}

		try {
			InetAddress address = InetAddress.getByName(ip);
			DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, 9);
			DatagramSocket socket = new DatagramSocket();

			for (int i = 0; i <= 10; i++) {
				socket.send(packet);
			}

			socket.close();

			log("Waked " + ip + " up");

		} catch (Exception e) {
			log("Wake up failed");
			e.printStackTrace();
		}
	}

	private static byte[] getMacBytes(String macStr) throws IllegalArgumentException {
		byte[] bytes = new byte[6];
		String[] hex = macStr.split("(\\:|\\-)");
		if (hex.length != 6) {
			throw new IllegalArgumentException("Invalid MAC address.");
		}

		try {
			for (int i = 0; i < 6; i++) {
				bytes[i] = (byte) Integer.parseInt(hex[i], 16);
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid hex digit in MAC address.");
		}
		return bytes;
	}

	public static WakeOnLan getInstance() {
		if (instance == null)
			instance = new WakeOnLan();
		return instance;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
