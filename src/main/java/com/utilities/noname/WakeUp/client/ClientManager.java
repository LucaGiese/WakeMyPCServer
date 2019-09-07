package com.utilities.noname.WakeUp.client;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientManager {

	private static ClientManager instance;

	private List<Client> clients = new ArrayList<Client>();
	
	public void createClient(Socket s) {
		Client c = new Client(s);
		clients.add(c);
	}
	
	public static ClientManager getInstance() {
		if(instance == null)
			instance = new ClientManager();
		return instance;
	}
	
}
