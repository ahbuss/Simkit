/*
 * SimkitXML2Java.java
 *
 * Created on March 23, 2004, 4:59 PM
 */

package simkit.xsd.translator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.ListIterator;
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

    /* convenience Strings for formatting */

    String sp  = " ";
    String sp4 = "    ";
    String sp8 = sp4+sp4;
    String nl  = new StringBuffer('\r').toString() + new StringBuffer('\n').toString();
    String bb  = "{";
    String eb  = "}";
    String sc  = ";";

    
    /** 
     * Creates a new instance of SimkitXML2Java 
     * when used from another class, instance this
     * with a String for the name of the xmlFile
     */

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

    public String translate(SimkitModuleElement sme) {
	StringBuffer source = new StringBuffer();
	StringWriter head = new StringWriter();
	StringWriter tail = new StringWriter();
	StringWriter vars = new StringWriter();
	StringWriter runBlock = new StringWriter();
	StringWriter eventBlock = new StringWriter();
	StringWriter accessorBlock = new StringWriter();

	buildHead(sme, head);
	buildVars(sme, vars);
	buildRunBlock(sme, runBlock);
	buildEventBlock(sme, eventBlock);
	buildAccessorBlock(sme, accessorBlock);
	buildSource(source, head, vars, runBlock, eventBlock, accessorBlock, tail);

	return source.toString();
    }

    void buildHead(SimkitModuleElement sme, StringWriter head) {
	PrintWriter pw = new PrintWriter(head);
	String name = sme.getName();
	
	pw.println("public class " + name + sp + bb);
    }

    void buildVars(SimkitModuleElement sme, StringWriter vars) {
	PrintWriter pw = new PrintWriter(vars);

	java.util.List variableDeclarations = sme.getVariableDeclarations();
	java.util.ListIterator li = variableDeclarations.listIterator();

	while ( li.hasNext() ) {

	    Object o = li.next();
	    
	    /* the dtd orders these already */

	    if ( o instanceof ParameterType ) {

		ParameterType p = (ParameterType) o;

		pw.println(nl + sp4 + "private" + sp + p.getType() + sp + p.getName() + sc);

	    } else if ( o instanceof StateVariableType ) {

		StateVariableType s = (StateVariableType) o;

		pw.println(nl + sp4 + "protected" + sp + s.getType() + sp + s.getName() + sc);

	    } else error ("bad content while building variable declarations");

		
	}

	    
    }

    void buildRunBlock(SimkitModuleElement sme, StringWriter runBlock) {
    
    }

    void buildEventBlock(SimkitModuleElement sme, StringWriter eventBlock) {
    
    }

    void buildAccessorBlock(SimkitModuleElement sme, StringWriter accessorBlock) {
    
    }

    void buildSource(StringBuffer source, StringWriter head, StringWriter vars, StringWriter runBlock, 
	StringWriter eventBlock, StringWriter accessorBlock, StringWriter tail ) {
   	 
	source.append(head.getBuffer()).append(vars.getBuffer()).append(runBlock.getBuffer());
	source.append(eventBlock.getBuffer()).append(accessorBlock.getBuffer()).append(tail.getBuffer());
    }

    public void writeOut(String data, java.io.PrintStream out) {
	out.println(data);	
    }

    void error(String desc) {

	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);

	pw.println("error :");
	pw.println(desc);

	System.err.println(sw.toString());
	System.exit(1);
    }

    /**
     * @param args the command line arguments
     * args[0] - XML file to translate
     * follow this pattern to use this class from another,
     * otherwise this can be used stand alone from CLI
     */

    public static void main(String[] args) {
	SimkitXML2Java sx2j = new SimkitXML2Java(args[0]);
	SimkitModuleElement simkitModule = sx2j.unmarshal();
	String dotJava = sx2j.translate(simkitModule);
	sx2j.writeOut(dotJava,System.out);
    }
    
}
