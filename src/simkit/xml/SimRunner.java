package simkit.xml;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import simkit.Schedule;
import simkit.StopType;
/**
 * Runs a simKit simulation as controled by a xml control file.
 * @author  ahbuss
 * @version $Id$
 */
public class SimRunner implements Runnable {
    
/**
* Creates a SimRunner from the information contained in the given root Element.
**/
    public static SimRunner getSimRunner(Element element) {
        if (element.getNodeType() == Node.DOCUMENT_NODE) {
            element = ((Document) element).getDocumentElement();
        }
        SimRunner runner = new SimRunner();
        Node verboseElement = element.getElementsByTagName("Verbose").item(0);
        if (verboseElement != null) {
            runner.verbose = new Boolean(verboseElement.getFirstChild().getNodeValue()).booleanValue();
        }
        else {
            runner.verbose = true;
        }
        Node singleStepElement = element.getElementsByTagName("SingleStep").item(0);
        if (singleStepElement != null) {
            runner.singleStep = new Boolean(singleStepElement.getFirstChild().getNodeValue()).booleanValue();
        }
        Node numberReplicationsElement = element.getElementsByTagName("NumberReplications").item(0);
        if (numberReplicationsElement == null) {
            runner.numberReplications = 1;
        }
        else {
            runner.numberReplications = Integer.parseInt(numberReplicationsElement.getFirstChild().getNodeValue());
        }
        Element stopNode = (Element) element.getElementsByTagName("StopType").item(0);
        if (stopNode == null || ! stopNode.hasChildNodes()) {
            runner.stopType = StopType.NO_STOP_TYPE;
        } // if null
        else {
            NodeList stopAtTimeNodeList = ((Element) stopNode).getElementsByTagName("StopAtTime");
            if (stopAtTimeNodeList.getLength() > 0) {
                runner.stopType = StopType.STOP_AT_TIME;
                Element stopTimeNode = (Element) stopAtTimeNodeList.item(0);
                stopTimeNode = (Element) stopTimeNode.getElementsByTagName("stopTime").item(0);
                if (stopTimeNode != null) {
                    runner.stopTime = Double.parseDouble(stopTimeNode.getFirstChild().getNodeValue());
                }
            }
            else {
                NodeList stopOneEventNodeList = ((Element) stopNode).getElementsByTagName("StopOnEvent");
                if (stopOneEventNodeList.getLength() > 0) {
                    runner.stopType = StopType.STOP_ON_EVENT;
                    Element stopEventNode = (Element) stopOneEventNodeList.item(0);
                    if (stopEventNode != null) {
                        runner.stopEvent = stopEventNode.getElementsByTagName("stopEvent").item(0).getFirstChild().getNodeValue();
                    }
                    Node stopEventCount = stopEventNode.getElementsByTagName("stopEventCount").item(0);
                    if (stopEventCount != null) {
                        runner.stopEventCount = Integer.parseInt(stopEventCount.getFirstChild().getNodeValue());
                    }
                    Element signatureNode = (Element) stopEventNode.getElementsByTagName("signature").item(0);
                    if (signatureNode != null) {
                        NodeList args = signatureNode.getElementsByTagName("class");
                        runner.stopEventSignature = new Class[args.getLength()];
                        for (int i = 0; i < args.getLength(); ++i) {
                            try {
                                runner.stopEventSignature[i] = Class.forName(args.item(i).getFirstChild().getNodeValue());
                            } catch (ClassNotFoundException e) { e.printStackTrace(System.err); }
                        }
                    }
                    else {
                        runner.stopEventSignature = new Class[0];
                    }
                }
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
    
/**
* Starts the simulation.
**/
    public void run() {
        if (stopType == StopType.STOP_AT_TIME) {
            Schedule.stopAtTime(stopTime);
        }
        else if (stopType == StopType.STOP_ON_EVENT) {
            Schedule.stopOnEvent(stopEvent, stopEventSignature, stopEventCount);
        }
        Schedule.setSingleStep(singleStep);
        Schedule.setVerbose(verbose);
        
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
    
/**
* A test method. 
**/
    public static void main(String[] args) throws Throwable {
        
        simkit.examples.ArrivalProcess arrival =
        new simkit.examples.ArrivalProcess(
            simkit.random.RandomVariateFactory.getInstance(
            "Exponential", new Object[] {new Double(1.7)}, 12345L));
        
        String filename = args.length > 0 ? args[0] : "simkit/xml/Run1.xml";
        InputStream inStream = 
            Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inStream);
        
        SimRunner runner = SimRunner.getSimRunner(document.getDocumentElement());
        
        System.out.println(runner);
        runner.run();
    }
}
