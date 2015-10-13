package server.core;

import java.net.URISyntaxException;

public class Main {
	private String mainDir;
	private String dataDir;
	
	/** Impossible to throw that {@link URISyntaxException} but still.... */
	private Main(){
		
		try {
			mainDir = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString().substring(6);
			dataDir = mainDir + "data/";
		} catch (URISyntaxException e) { /* Impossible */ e.printStackTrace(); }
		
		// Load Config;
		Config.getInstance();
	}
	
	
	public String getMainDir(){
		return mainDir;
	}
	
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
