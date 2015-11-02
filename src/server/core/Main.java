package server.core;


import java.io.File;
import java.net.URISyntaxException;

import braynstorm.commonlib.Logger;
import server.network.Server;
public class Main {
	private static String mainDir = "";
	private static String dataDir = "";
	
	private Thread serverThread;
	private Server server;
	
	private Main(){
		
		try {
			mainDir = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString().substring(6); // StackOverflow :)
			dataDir = mainDir + "data/";
			
			File dataDirDir = new File(dataDir);
			if(!dataDirDir.isDirectory()){
			    dataDirDir.mkdir();
			}
			
		} catch (URISyntaxException e) { /* Impossible */ e.printStackTrace(); }
		
		Logger.init(mainDir);
		Config.init();
		
		
		server = new Server();
		serverThread = new Thread(server);
		serverThread.setName("Networking");
		serverThread.run();
	}
	
	public static Server getServer(){
	    return getInstance().server;
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
