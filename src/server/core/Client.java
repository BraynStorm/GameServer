package server.core;

import java.net.Socket;

/**
 * Describes a TCP connection from the client to the server.
 * @author BraynStorm
 * 
 */
public class Client {
	private Socket socket;
	private int accountID;
	
	public Client(Socket socket) {
		this.socket = socket;
	}
	
	public boolean isLogged(){
		return accountID == 0;
	}
	
	
	
}
