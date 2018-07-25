package io.warp10.spark;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.LogManager;

import org.apache.spark.SparkFiles;

import io.warp10.WarpConfig;
import io.warp10.script.WarpScriptLib;

public class Warp10Spark {
  
  private static final String DISABLE_LOGGING = "disable.logging";
  
  public static void init() {
    try {      
      if (null != System.getProperty(WarpConfig.WARP10_CONFIG)) {
        InputStream in = Warp10Spark.class.getClassLoader().getResourceAsStream(System.getProperty(WarpConfig.WARP10_CONFIG));
        
        if (null == in) {
          try {
            in = new FileInputStream(SparkFiles.get(System.getProperty(WarpConfig.WARP10_CONFIG)));
          } catch (IOException ioe) {            
          }
        }
        
        if (null == in) {
          in = new FileInputStream(System.getProperty(WarpConfig.WARP10_CONFIG));
        }

        WarpConfig.safeSetProperties(new InputStreamReader(in));
      //} else if (System.getenv(WarpConfig.WARP10_CONFIG_ENV))
      } else if (null != System.getenv("WARP10_CONFIG")) {
        InputStream in = Warp10Spark.class.getClassLoader().getResourceAsStream(System.getenv("WARP10_CONFIG"));

        if (null == in) {
          try {
            in = new FileInputStream(SparkFiles.get(System.getenv("WARP10_CONFIG")));
          } catch (IOException ioe) {           
          }
        }
        
        if (null == in) {
          in = new FileInputStream(System.getenv("WARP10_CONFIG"));
        }
        
        WarpConfig.safeSetProperties(new InputStreamReader(in));        
      } else {
        WarpConfig.safeSetProperties((String) null);
      }
      
      //
      // Register extensions
      //
      
      WarpScriptLib.registerExtensions();
      
      //
      // Disable logging
      //
      if ("true".equals(System.getProperty(DISABLE_LOGGING))) {
        LogManager.getLogManager().reset();
      }
      
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
