package org.bsth.util;


import java.io.IOException;
import java.util.Properties;

public class Tools {  
    private static Properties p = new Properties();  
    private static String f;
    public Tools(String file){
    	f = file;
    	try {
			p.load(Tools.class.getClassLoader().getResourceAsStream(f));
		} catch (IOException e) {
			e.printStackTrace();
		}  
    }
  
    public String getValue(String key)  
    {  
        return p.getProperty(key);  
    }  
}  
