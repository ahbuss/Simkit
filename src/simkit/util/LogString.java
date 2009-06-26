/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simkit.util;

import simkit.BasicEventList;

/**
 *
 * @author Kirk Stork, The MOVES Institute, Naval Postgraduate School
 */
public class LogString {

    public static String format(BasicEventList schedule, String message) {
        StringBuffer result;
        result = new StringBuffer();
        result.append(schedule.getSimTime());
        result.append("\n\t" + message + "\n");
        return result.toString();
    }

    public static String format(String timeStr, String message) {
        StringBuffer result;
        result = new StringBuffer();
        result.append(timeStr);
        result.append("\n\t" + message + "\n");
        return result.toString();
    }

}
