package server.core;

import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The beer that brought the forest down.
 * @author BraynStorm
 *
 */
public class Logger implements Closeable {
	private PrintWriter logFile;
	
	public void log(LogLevel level, String message){
		String str = level.toString() + ": " + message + '\n';
		
		logFile.write(str);
		System.out.println(str);
	}
	
	public void log(Exception e){
		e.printStackTrace(logFile);
		e.printStackTrace();
	}
	
	private Logger() {
		// Initialization
	    
		try{
			logFile = new PrintWriter(new FileOutputStream(Config.getInstance().getValueS("logFolder") + "latest.log"));
		}catch(IOException e){
			e.printStackTrace();
			System.exit(0);
		}
		
	}

	// Singleton
	private static Logger instance;

	public static Logger getInstance() {
		if (instance == null)
			instance = new Logger();
		return instance;
	}


	@Override
	public void close() throws IOException {
		logFile.close();
	}
	
	
	public enum LogLevel{
		IMPOSSIBRU("IMPOSSIBRU"),
		CRITICAL("CRITICAL"),
		WARNING("WARNING"),
		INFO("INFO"),
		DEBUG("DEBUG");
		
		private String name;
		
		private LogLevel(String s){
			name = s;
		}
		
		@Override
		public String toString(){
			return name;
		}
	}
}
