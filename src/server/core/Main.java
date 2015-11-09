package server.core;


import java.io.File;
import java.net.URISyntaxException;
import java.sql.SQLException;

import braynstorm.commonlib.Logger;
import server.core.db.Database;
import server.game.World;
import server.network.Server;
public class Main {
	private static String mainDir = "";
	private static String dataDir = "";
	
	private Thread serverThread;
	private Server server;
	private World world;
	private Database database;
	
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
		Logger.setLogEverything(true);
		Config.init();
		
		database = new Database();
		try {
            Logger.logInfo(database.accountExists("glav0r3zzz4@gmail.com"));
        } catch (SQLException e) {
            Logger.logExceptionCritical(e);
        }
		
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

    public static World getWorld() {
        return getInstance().world;
    }

    public static Database getDatabase() {
        return getInstance().database;
    }
}
