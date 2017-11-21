/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simkit.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import simkit.Named;
import simkit.Schedule;
import simkit.SimEvent;
import simkit.SimEventSource;

/**
 * A Simple event and state change listener that does nothing
 * but record events and state changes to a text file.  Intended as a
 * Gui-free way to examine and compare model behavior from run to run.
 * 
 * @author Kirk Stork, The Moves Institute
 */
public class ModelTrajectoryLogger implements simkit.SimEventListener, PropertyChangeListener
{
    public static final String DEFAULT_TRAJECTORY_FILE_NAME = "SimkitModelTrajectory.txt";
    
    protected File trajectoryFile;
    protected PrintStream out;

    public ModelTrajectoryLogger(){
        try {
            out = new PrintStream(new File("ModelTrace.txt"));
        } catch (FileNotFoundException ex){
            Logger.getLogger(ModelTrajectoryLogger.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public ModelTrajectoryLogger(File logFile){
        try {
            out = new PrintStream(logFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ModelTrajectoryLogger.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
    public void processSimEvent(SimEvent e) {
        String entityName = ((Named)e.getSource()).getName();
        StringBuilder msg = new StringBuilder(Schedule.getSimTimeStr());
        msg.append(" EVENT        " + entityName+ "#" + e.getEventName());
        if (e.getParameters().length > 0){
            msg.append("\n       PARAMETERS   " + Arrays.toString(e.getParameters()));
        }
        out.println(msg.toString());
    }

    public void propertyChange(PropertyChangeEvent e) {
        Named entity = (Named)e.getSource();
        Object oval = e.getOldValue();
        Object nval = e.getNewValue();
        
        if (null == oval){oval = "Null";}
        if (null == nval){nval = "Null";}
        
        if (nval instanceof Named){
            nval = ((Named)nval).getName();
        }
        StringBuilder msg = new StringBuilder(Schedule.getSimTimeStr());
        msg.append(" STATE CHANGE " + entity.getName()+ "#" + e.getPropertyName() + "(Class " + entity.getClass().getCanonicalName() + ")");
        msg.append("\n       FROM         " + oval);
        msg.append("\n       TO           " + nval);
        out.println(msg.toString());
        //System.out.println("New property is of class " + nval.getClass());
    }

}
