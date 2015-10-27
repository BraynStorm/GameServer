package server.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

import server.core.utils.Defaults;

public class ClientAcceptor implements Runnable {
	
	private HashSet<Client> clients;
	
	private ServerSocket serverSocket;
	private boolean running;
	
	public ClientAcceptor() {
		clients = new HashSet<>();
		try {
			serverSocket = new ServerSocket(Config.getInstance().getValuei("serverPort"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(serverSocket != null){
				
			}
		}
	}
	
	@Override
	public void run() {
		// If the Acceptor is running...
		while(running)
			// and we aren't full...
			if(clients.size() <= Defaults.MAX_CLIENT_CONNECTIONS){
				try{
					Socket s = serverSocket.accept();
					clients.add(new Client(s));
				}catch(IOException e){
					e.printStackTrace();
				}
				
			}
	}
}
