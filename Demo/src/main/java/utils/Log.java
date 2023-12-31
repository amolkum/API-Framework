package utils;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.LogManager;


public class Log {

    final private static Logger Log = Logger.getLogger(Log.class.getName());
    //DOMConfigurator.configure(System.getProperty("user.dir")+"\\src\\test\\log4j.xml");


    //This is to print log for the ending of the test case


    // Need to create these methods, so that they can be called

    public static void info(String message) {

        Log.info(message);

    }

    public static void warn(String message) {

        Log.warn(message);

    }

    public static void error(String message) {

        Log.error(message);

    }

    public static void fatal(String message) {

        Log.fatal(message);

    }

    public static void debug(String message) {

        Log.debug(message);

    }

}
