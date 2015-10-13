package server.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;

public class Config {
	
	private boolean wasConfigChanged = false;
	
	private HashMap<String, Integer> integers;
	private HashMap<String, Float> floats;
	private HashMap<String, String> strings;
	
	private HashMap<String, Integer> defaultIntegers;
	private HashMap<String, Float> defaultFloats;
	private HashMap<String, String> defaultStrings;
	
	private Config() {
		defaultIntegers = new HashMap<String, Integer>();
		defaultFloats = new HashMap<String, Float>();
		defaultStrings = new HashMap<String, String>();
		
		initDefaults();
		
		integers = new HashMap<String, Integer>();
		floats = new HashMap<String, Float>();
		strings = new HashMap<String, String>();
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader(Main.getInstance().getMainDir()));
			StringBuilder stringBuilder = new StringBuilder();
			
			while(reader.ready())
				stringBuilder.append(reader.readLine()).append('\n');
			
			reader.close();
			
			mapJSON(new JSONObject(stringBuilder.toString()));
			
		} catch (IOException e){
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private void mapJSON(JSONObject jsonObj){
		Iterator<String> it = jsonObj.keys();
		
		it.forEachRemaining(key -> {
			JSONObject innerObj = jsonObj.getJSONObject(key);
			Iterator<String> innerIt = innerObj.keys();
			switch(key){
				case "i":
					innerIt.forEachRemaining(integerKey -> integers.put(integerKey, innerObj.getInt(integerKey)) );
					break;
				case "f":
					innerIt.forEachRemaining(floatKey -> floats.put(floatKey, (float)innerObj.getDouble(floatKey)) );
					break;
				case "s":
					innerIt.forEachRemaining(stringKey -> strings.put(stringKey, innerObj.getString(stringKey)) );
					break;
			}
			
		});
	}
	
	private void initDefaults(){
		//TODO Initialize the default value for the whole conifg.
		
	}
	
	/**
	 * Heh, not really.
	 */
	public void destroy(){
		
		if(wasConfigChanged){
			JSONObject finalObj = new JSONObject();
			JSONObject integersObj = new JSONObject();
			JSONObject floatsObj = new JSONObject();
			JSONObject stringsObj = new JSONObject();
			
			// Closures are OP!
			integers.forEach((k, v) ->{   integersObj.put(k, v);   });
			floats.forEach((k, v) ->{   floatsObj.put(k, v);   });
			strings.forEach((k, v) ->{   stringsObj.put(k, v);   });
			
			finalObj.put("i", integersObj);
			finalObj.put("f", floatsObj);
			finalObj.put("s", stringsObj);
			
			try{
				FileWriter fileWriter = new FileWriter(Main.getInstance().getMainDir() + "config.cfg");
				
				fileWriter.write(finalObj.toString() + "\n");
				//DEBUG
				System.out.println(finalObj.toString() + "\n");
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		
		instance = null;
	}
	
	public int getValuei(String key){ return integers.get(key);	}
	public float getValuef(String key){ return floats.get(key); }
	public String getValueS(String key){ return strings.get(key); }
	
	public void setValuei(String key, int value){ int val = integers.put(key, value); wasConfigChanged = val != value; }
	public void setValuef(String key, float value){ float val = floats.put(key, value); wasConfigChanged = val != value; }
	public void setValueS(String key, String value){ String val = strings.put(key, value); wasConfigChanged = !val.equals(value); }
	
	// TODO Create the resetValue methods.
	/** NYI */
	public void resetValuei(String key){}
	/** NYI */
	public void resetValuef(String key){}
	/** NYI */
	public void resetValueS(String key){}
	
	// Singleton
	private static Config instance;

	public static Config getInstance() {
		if (instance == null)
			instance = new Config();
		return instance;
	}
}
