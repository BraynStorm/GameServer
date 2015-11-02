package server.core.utils;

import java.io.File;

import braynstorm.commonlib.math.Vector3f;

public class Common {
    
    public static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);

    public static void createFolder(String folder){
        File f = new File(folder);
        if(!f.isDirectory()){
            f.mkdirs();
        }
    }
    
}
