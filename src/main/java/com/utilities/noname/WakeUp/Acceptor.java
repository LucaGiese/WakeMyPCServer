package com.utilities.noname.WakeUp;

import java.io.IOException;
import java.net.Socket;

import com.utilities.noname.WakeUp.client.ClientManager;

public class Acceptor extends Logger {

	private Thread acceptorThread;
	private static Acceptor instance;

	public void start() {
		acceptorThread = new Thread(new Runnable() {
			public void run() {
				log("Acceptor successfully started");
				while (WakeOnLan.getInstance().isRunning()) {
					try {
						log("Waiting for new Clients");
						Socket socket = WakeOnLan.getInstance().getServerSocket().accept();
						log("New Client connected to Server");

						ClientManager.getInstance().createClient(socket);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		acceptorThread.start();
	}

	@SuppressWarnings("deprecation")
	public void stop() {
		acceptorThread.stop();
	}

	public static Acceptor getInstance() {
		if (instance == null)
			instance = new Acceptor();
		return instance;
	}
}
