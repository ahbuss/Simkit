/*
 * SimkitXML2Java.java
 *
 * Created on March 23, 2004, 4:59 PM
 */

package simkit.xsd.translator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
// some reason, Netbeans fails to find these. Might be
// the replacement DOM or SAX?
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import simkit.xsd.bindings.*;

/**
 *
 * @author  Rick Goldberg
 *
 */

public class SimkitXML2Java {

    FileInputStream fileInputStream;
    JAXBContext jaxbCtx;
    
    /** Creates a new instance of SimkitXML2Java */
    public SimkitXML2Java(String xmlFile) {
	try {
            jaxbCtx = JAXBContext.newInstance("simkit.xsd.bindings");
	    fileInputStream = new FileInputStream(xmlFile);
	} catch ( Exception e ) {
	    e.printStackTrace();
	} 
	
    }
    
    public SimkitModuleElement unmarshal() {
	SimkitModuleElement s=null;
	Unmarshaller u;
	try {
	    u = jaxbCtx.createUnmarshaller();
	    s = (SimkitModuleElement) u.unmarshal(fileInputStream);
	} catch (Exception e) { e.printStackTrace(); }
	return s;
    }  

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) {
	SimkitXML2Java sx2j = new SimkitXML2Java(args[0]);
	SimkitModuleElement simkitModule = sx2j.unmarshal();
    }
    
}
