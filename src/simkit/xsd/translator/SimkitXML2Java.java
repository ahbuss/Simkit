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
import java.util.Vector;
import java.util.Enumeration;
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

    private Vector params = new Vector();
    private Vector states = new Vector();

    FileInputStream fileInputStream;
    JAXBContext jaxbCtx;

    /* convenience Strings for formatting */

    String sp  = " ";
    String sp4 = sp+sp+sp+sp;
    String sp8 = sp4+sp4;
    String ob  = "{";
    String cb  = "}";
    String sc  = ";";
    String cm  = ",";
    String lp  = "(";
    String rp  = ")";
    String eq  = "=";

    
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
	buildVars(sme, vars, accessorBlock);
	buildRunBlock(sme, runBlock);
	buildEventBlock(sme, eventBlock);
	//buildAccessorBlock(sme, accessorBlock);
	buildSource(source, head, vars, runBlock, eventBlock, accessorBlock, tail);

	return source.toString();
    }

    void buildHead(SimkitModuleElement sme, StringWriter head) {

	PrintWriter pw = new PrintWriter(head);
	String name = sme.getName();
	
	pw.println("package examples;");
	pw.println();
	pw.println("import simkit.*;");
	pw.println("import simkit.random.*;");
	pw.println("import java.util.*;");
	pw.println();
	pw.println("public class " + name + sp + "extends SimEntityBase" + sp + ob);
	pw.println();
    }

    void buildVars(SimkitModuleElement sme, StringWriter vars, StringWriter accessorBlock) {

	PrintWriter pw = new PrintWriter(vars);
	boolean gap = true;

	List variableDeclarations = sme.getVariableDeclarations();
	ListIterator li = variableDeclarations.listIterator();

	while ( li.hasNext() ) {

	    Object o = li.next();
	    
	    /* the dtd orders these already */

	    if ( o instanceof ParameterType ) {

		ParameterType p = (ParameterType) o;

		pw.println(sp4 + "private" + sp + p.getType() + sp + p.getName() + sc);

		this.params.add(p);
	
		createParameterAccessor(p,accessorBlock);

	    } else if ( o instanceof StateVariableType ) {

		StateVariableType s = (StateVariableType) o;

		if (gap) { 
		    pw.println();
		    gap = false;
		}

		pw.println(sp4 + "protected" + sp + s.getType() + sp + s.getName() + sc);

		this.states.add(s);

	    } else error ("bad content while building variable declarations");
	}

	pw.println();
	pw.println(sp4 + "/** Creates a new instance of " + sme.getName() + " */");
	pw.println();
	pw.print(sp4 + "public " + sme.getName() + "(");
	    
	Enumeration e = params.elements();

	while ( e.hasMoreElements() ) {

	    ParameterType pt = (ParameterType) e.nextElement();
		
	    pw.print(pt.getType() + sp + pt.getShortName());
	
 	    if ( pt != params.lastElement() && params.size() > 1 ) {
	        pw.print(cm);
		pw.println();
		pw.print(sp8);
	    }
	}

	pw.println(") {");

    }

    void createParameterAccessor(ParameterType p, StringWriter sw) {

	PrintWriter pw = new PrintWriter(sw);
	
	pw.print(sp4 + "public void set" + capitalize(p.getName()) + lp);	
	pw.print(p.getType() + sp + p.getShortName() + rp + sp + ob + sp);
	pw.println(p.getName() + sp + eq + sp + p.getShortName() + sc + sp + cb);
	pw.print(sp4 + "public " + p.getType() + sp + "get" + capitalize(p.getName()) );
	pw.println(lp + rp + sp + ob + sp + "return" + sp + p.getName() + sc + sp + cb);
	pw.println();
    }

    void buildRunBlock(SimkitModuleElement sme, StringWriter runBlock) {
	
	PrintWriter pw = new PrintWriter(runBlock);
	List events = sme.getEvent();
	ListIterator li = events.listIterator();
	EventType e = null;

	while ( li.hasNext() ) {
	
	    /* run should usually be the first Event */

	    e = (EventType) li.next();
	
	    if ( e.getName().equals("Run") ) break;

	}
	
	pw.println(sp4 + "/** Set initial values of all state variables */");
	pw.println(sp4 + "public void reset() {");
	pw.println();
    
    }

    void buildEventBlock(SimkitModuleElement sme, StringWriter eventBlock) {
    
    }

    void buildParameterAccessor(ParameterType p, StringWriter accessorBlock) {
    
    }

    void buildSource(StringBuffer source, StringWriter head, StringWriter vars, StringWriter runBlock, 
	StringWriter eventBlock, StringWriter accessorBlock, StringWriter tail ) {
   	 
	source.append(head.getBuffer()).append(vars.getBuffer()).append(runBlock.getBuffer());
	source.append(eventBlock.getBuffer()).append(accessorBlock.getBuffer()).append(tail.getBuffer());
    }

    public void writeOut(String data, java.io.PrintStream out) {
	out.println(data);	
    }

    private String capitalize( String s ) {
        return s.substring(0,1).toUpperCase() + s.substring(1);
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
