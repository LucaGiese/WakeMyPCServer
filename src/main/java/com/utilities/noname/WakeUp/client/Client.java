package com.utilities.noname.WakeUp.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.utilities.noname.WakeUp.Key;
import com.utilities.noname.WakeUp.Logger;
import com.utilities.noname.WakeUp.WakeOnLan;

public class Client extends Logger{

	@SuppressWarnings("unused")
	private Socket socket;

	private ObjectOutputStream out;
	private ObjectInputStream in;

	private Thread thread;

	public Client(Socket socket) {
		this.socket = socket;

		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			startReading();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startReading() {
		thread = new Thread(new Runnable() {
			public void run() {
				try {
					log("Now Reading Incoming");
					
					String line = null;
					while ((line = in.readUTF()) != null) {
						
						if(line.equals(Key.key)) {
							log("Key Received");
							send("Key was successfully received, thanks");
							
							WakeOnLan.getInstance().wakeUp();
						}else {
							log("Unknown String received");
						}
					}
				} catch (IOException e) {
					System.out.println(e);
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	public void send(String msg) {
		try {
			out.writeUTF(msg);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
