/*
 * SimRunner.java
 *
 * Created on June 26, 2002, 11:45 AM
 */

package simkit.xml;
import simkit.*;
import java.util.*;
import org.jdom.*;
import org.jdom.input.*;
import java.io.*;
/**
 *
 * @author  ahbuss
 */
public class SimRunner implements Runnable {
    
    public static SimRunner getSimRunner(Element element) {
        if (element == null) {
            element = new Element("run");
        }
        SimRunner runner = new SimRunner();
        Element verboseElement = element.getChild("Verbose");
        if (verboseElement != null) {
            runner.verbose = new Boolean(verboseElement.getText()).booleanValue();
        }
        else {
            runner.verbose = true;
        }
        
        Element singleStepElement = element.getChild("SingleStep");
        if (singleStepElement != null) {
            runner.singleStep = new Boolean(singleStepElement.getText()).booleanValue();
        }
        Element numberReplicationsElement = element.getChild("NumberReplications");
        if (numberReplicationsElement == null) {
            runner.numberReplications = 1;
        }
        else {
            runner.numberReplications = Integer.parseInt(numberReplicationsElement.getText());
        }
        Element stopTypeElement = element.getChild("StopType");
        if (stopTypeElement == null) {
            stopTypeElement = new Element("StopType");
        } // if null
        List stops = stopTypeElement.getChildren();
        if (stops.isEmpty()) {
            stopTypeElement.addContent(new Element("NoStopTime"));
        }
        stopTypeElement = (Element) stopTypeElement.getChildren().get(0);
        if (stopTypeElement != null) {
            String type = stopTypeElement.getName();
            if (type.equalsIgnoreCase("StopAtTime")) {
                runner.stopType = StopType.STOP_AT_TIME;
                String stopTimeText = stopTypeElement.getChild("stopTime").getText();
                runner.stopTime = Double.parseDouble(stopTimeText);
            } // if StopAtTime
            else if (type.equalsIgnoreCase("StopOnEvent")) {
                runner.stopType = StopType.STOP_ON_EVENT;
                runner.stopEvent = stopTypeElement.getChild("stopEvent").getText();
                String stopEventCountText = stopTypeElement.getChild("stopEventCount").getText();
                runner.stopEventCount = Integer.parseInt(stopEventCountText);
                Element signature = stopTypeElement.getChild("signature");
                if (signature != null) {
                    List classes = signature.getChildren("class");
                    runner.stopEventSignature = new Class[classes.size()];
                    for (int i = 0; i < runner.stopEventSignature.length; i++) {
                        try {
                            runner.stopEventSignature[i] = Class.forName(((Element) classes.get(i)).getText());
                        } catch (ClassNotFoundException e) { System.err.println(e); }
                    } // for
                } // if signature == null
                else {
                    runner.stopEventSignature = new Class[0];
                }
            } // if StopOnEvent
            else if (type.equalsIgnoreCase("NoStopTime")) {
                runner.stopType = StopType.NO_STOP_TYPE;
            }
            else {
                runner.stopType = StopType.NO_STOP_TYPE;
            }
        }
        
        return runner;
    }
    
    protected boolean verbose;
    protected boolean singleStep;
    protected double stopTime;
    protected boolean stopOnEvent;
    protected String stopEvent;
    protected int stopEventCount;
    protected Class[] stopEventSignature;
    protected StopType stopType;
    protected int numberReplications;
    
    /** Creates a new instance of SimRunner */
    protected SimRunner() {
    }
    
    public void run() {
        if (stopType == StopType.STOP_AT_TIME) {
            Schedule.stopAtTime(stopTime);
        }
        else if (stopType == StopType.STOP_ON_EVENT) {
            Schedule.stopOnEvent(stopEvent, stopEventSignature, stopEventCount);
        }
        Schedule.setVerbose(verbose);
        Schedule.setSingleStep(singleStep);
        
        for (int i = 0; i < numberReplications; i++) {
            Schedule.reset();
            Schedule.startSimulation();
        }
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer("Parameters for Simulation Run:\n");
        buf.append("\tverbose = ");
        buf.append(verbose);
        buf.append("\n\tsingleStep = ");
        buf.append(singleStep);
        buf.append("\n\tStopType = ");
        buf.append(stopType);
        if (stopType == StopType.STOP_AT_TIME) {
            buf.append("\n\tstop time = ");
            buf.append(stopTime);
        }
        else if (stopType == StopType.STOP_ON_EVENT) {
            buf.append("\n\tStop Event = ");
            buf.append(stopEvent);
            buf.append("\n\tevent count = ");
            buf.append(stopEventCount);
            if (stopEventSignature.length > 0) {
                buf.append("\n\tsignature = (");
                for (int i = 0; i < stopEventSignature.length; i++) {
                    buf.append(stopEventSignature[i].getName());
                    if (i < stopEventSignature.length - 1) { buf.append(", "); }
                }
                buf.append(')');
            }
        }
        buf.append("\n\tnumber replications = ");
        buf.append(numberReplications);
        buf.append('\n');
        return buf.toString();
    }
    
    public static void main(String[] args) throws Throwable {
        
        System.out.println("Testing with null Element:\n");
        SimRunner.getSimRunner(null).run();

        simkit.examples.ArrivalProcess arrival = 
            new simkit.examples.ArrivalProcess("Exponential", new Object[] {new Double(1.7)}, 12345L);
        
        String filename = args.length > 0 ? args[0] : "Run1.xml";
        InputStream inStream = SimRunner.class.getResourceAsStream(filename);
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(inStream);
        Element root = doc.getRootElement();
        SimRunner runner = SimRunner.getSimRunner(root);
        System.out.println(runner);
        runner.run();
        
    }
    
}
