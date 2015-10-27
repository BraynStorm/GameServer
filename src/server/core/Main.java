package server.core;

import java.net.URISyntaxException;

public class Main {
	private static String mainDir = "";
	private static String dataDir = "";
	
	private static ClientAcceptor clientAcceptor;
	private static Thread clientAcceptorThread;
	
	/** Impossible to throw that {@link URISyntaxException} but still.... */
	private Main(){
		
		try {
			mainDir = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString().substring(6); // StackOverflow :)
			dataDir = mainDir + "data/";
		} catch (URISyntaxException e) { /* Impossible */ e.printStackTrace(); }
		
		// Load Config;
		Config.getInstance();
		
		
		// Start accepting clients.
		clientAcceptor = new ClientAcceptor();
		clientAcceptorThread = new Thread(clientAcceptor);
		clientAcceptorThread.setName("ClientAcceptor");
		clientAcceptorThread.start();
	}
	
	
	
	// Getters
	public static String getMainDir(){ return mainDir; }
	public static String getDataDir(){ return dataDir; }
	
	// Singleton
	private static Main instance;

	public static Main getInstance() {
		if (instance == null)
			instance = new Main();
		return instance;
	}
	
	public static void main(String[] args){
		Main.getInstance();
	}
}
