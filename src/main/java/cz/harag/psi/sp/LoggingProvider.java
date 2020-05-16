package cz.harag.psi.sp;

import java.time.LocalTime;

/**
 * @author Patrik Harag
 * @version 2020-05-16
 */
public class LoggingProvider {

    public static void log(String msg) {
        System.out.println(LocalTime.now() + " | " + msg);
    }

    public static void logResponse(String response) {
        System.out.println(LocalTime.now() + " | << " + response);
    }

}
