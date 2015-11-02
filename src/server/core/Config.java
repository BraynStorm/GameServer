package server.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;

public class Config {
    private static boolean wasConfigChanged = false;
    
    private static HashMap<String, Integer> integers;
    private static HashMap<String, Float> floats;
    private static HashMap<String, String> strings;
    
    private static HashMap<String, Integer> defaultIntegers;
    private static HashMap<String, Float> defaultFloats;
    private static HashMap<String, String> defaultStrings;
    
    private static boolean I = false;
    
    public static void init(){
        if(I)
            return;
        
        defaultIntegers = new HashMap<String, Integer>();
        defaultFloats = new HashMap<String, Float>();
        defaultStrings = new HashMap<String, String>();
        
        initDefaults();
        
        integers = new HashMap<String, Integer>();
        floats = new HashMap<String, Float>();
        strings = new HashMap<String, String>();
        
        try{
            BufferedReader reader = new BufferedReader(new FileReader(Main.getMainDir() + "/config.cfg"));
            StringBuilder stringBuilder = new StringBuilder();
            
            while(reader.ready())
                stringBuilder.append(reader.readLine()).append('\n');
            
            reader.close();
            String data = stringBuilder.toString();
            if(data.length() < 2)
                useDefaults();
            else
                mapJSON(new JSONObject(data));
            
        } catch (IOException e){
            useDefaults();
            Logger.logExceptionWarning(e);
        }
        
        I = true;
    }
    
    
    private static void useDefaults (){
        integers = new HashMap<>(defaultIntegers);
        floats = new HashMap<>(defaultFloats);
        strings = new HashMap<>(defaultStrings);
    }
    
    private static void mapJSON(JSONObject jsonObj){
        Iterator<String> it = jsonObj.keys();
        
        it.forEachRemaining(key -> {
            JSONObject innerObj = jsonObj.getJSONObject(key);
            Iterator<String> innerIt = innerObj.keys();
            switch(key.toLowerCase()){
                case "i":
                    innerIt.forEachRemaining(integerKey -> integers.put(integerKey, innerObj.getInt(integerKey)) );
                    break;
                case "f":
                    innerIt.forEachRemaining(floatKey -> floats.put(floatKey, (float)innerObj.getDouble(floatKey)) );
                    break;
                case "s":
                    innerIt.forEachRemaining(stringKey -> strings.put(stringKey, innerObj.getString(stringKey)) );
                    break;
                default:
                    System.out.println("Unknown config key. Skipping");
            }
            
        });
    }
    
    private static void initDefaults(){
        defaultIntegers.put("serverPort", 33055);
    }
    
    /**
     * Heh, not really.
     */
    public static void destroy(){
        
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
                FileWriter fileWriter = new FileWriter(Main.getMainDir() + "/config.cfg");
                
                fileWriter.write(finalObj.toString() + "\n");
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e){
                Logger.logExceptionWarning(e);
            }
        }
    }
    
    public static int getValuei(String key){ return integers.get(key); }
    public static float getValuef(String key){ return floats.get(key); }
    public static String getValueS(String key){ return strings.get(key); }
    
    public static void setValuei(String key, int value){ int val = integers.put(key, value);      wasConfigChanged = (val != value) || wasConfigChanged; }
    public static void setValuef(String key, float value){ float val = floats.put(key, value);    wasConfigChanged = (val != value) || wasConfigChanged; }
    public static void setValueS(String key, String value){ String val = strings.put(key, value); wasConfigChanged = (!val.equals(value)) || wasConfigChanged; }
    
    public static void resetValuei(String key){ setValuei(key, defaultIntegers.get(key)); wasConfigChanged = true; }
    public static void resetValuef(String key){ setValuef(key, defaultFloats.get(key));   wasConfigChanged = true; }
    public static void resetValueS(String key){ setValueS(key, defaultStrings.get(key));  wasConfigChanged = true; }

}
