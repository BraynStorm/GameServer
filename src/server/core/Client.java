package server.core;

import java.net.Socket;

/**
 * Describes a TCP connection from the client to the server.
 * @author BraynStorm
 * 
 */
public class Client implements Runnable{
	private Socket socket;
	private int accountID;
	
	public Client(Socket socket) {
		this.socket = socket;
	}
	
	public boolean isLogged(){
		return accountID == 0;
	}
	
	public int getAccountID(){
	    return accountID;
	}

    @Override
    public void run() {
        if(socket == null){
            return;
        }
        
        while ( !socket.isClosed() ){
            
        }
    }
	
	
	
}
