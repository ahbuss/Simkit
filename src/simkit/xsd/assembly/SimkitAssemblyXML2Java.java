/*
 * SimkitAssemblyXML2Java.java
 *
 * Created on April 1, 2004, 10:09 AM
 */

package simkit.xsd.assembly;

import java.io.InputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.ListIterator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import simkit.xsd.bindings.assembly.*;

/**
 *
 * @author  Rick Goldberg
 *
 */

public class SimkitAssemblyXML2Java {

    private SimkitAssemblyType root;

    InputStream fileInputStream;
    JAXBContext jaxbCtx;

    /* convenience Strings for formatting */

    String sp  = " ";
    String sp4 = sp+sp+sp+sp;
    String sp8 = sp4+sp4;
    String sp12 = sp8+sp4;
    String sp16 = sp8+sp8;
    String ob  = "{";
    String cb  = "}";
    String sc  = ";";
    String cm  = ",";
    String lp  = "(";
    String rp  = ")";
    String eq  = "=";
    String pd  = ".";
    String qu  = "\"";
    String nw = "new";

    
    /** 
     * Creates a new instance of SimkitXML2Java 
     * when used from another class, instance this
     * with a String for the name of the xmlFile
     */

    public SimkitAssemblyXML2Java(String xmlFile) {
	try {
            jaxbCtx = JAXBContext.newInstance("simkit.xsd.bindings.assembly");
            fileInputStream = Class.forName("simkit.xsd.assembly.SimkitAssemblyXML2Java").getClassLoader().getResourceAsStream(xmlFile);
	} catch ( Exception e ) {
	    e.printStackTrace();
	} 
	
    }
    
    public void unmarshal() {
	Unmarshaller u;
	try {
	    u = jaxbCtx.createUnmarshaller();
	    this.root = (SimkitAssemblyType) u.unmarshal(fileInputStream);
	} catch (Exception e) { e.printStackTrace(); }
    }  

    public String translate() {

	StringBuffer source = new StringBuffer();
	StringWriter head = new StringWriter();
	StringWriter tail = new StringWriter();
	StringWriter entities = new StringWriter();
	StringWriter listeners = new StringWriter();
	StringWriter connectors = new StringWriter();
	StringWriter output = new StringWriter();

	buildHead(head);
	buildEntities(entities);
	buildListeners(listeners);
	buildConnectors(connectors);
	buildOutput(output);
	buildTail(tail);

	buildSource(source, head, entities, listeners, connectors, output, tail);

	return source.toString();
    }

    void buildHead(StringWriter head) {

	PrintWriter pw = new PrintWriter(head);
	String name = this.root.getName();
	
	pw.println("package examples;");
	pw.println();
	pw.println("import simkit.*;");
	pw.println("import simkit.random.*;");
	pw.println("import simkit.stat.*;");
	pw.println("import simkit.util.*;");
	pw.println("import java.text.*;");
	pw.println();
	pw.println("public class " + name + sp + ob);
	pw.println();
	pw.println(sp4 + "public static void main(String[] args) {");
	pw.println();

    }

    void buildEntities(StringWriter entities) {
	PrintWriter pw = new PrintWriter(entities);
	ListIterator seli = this.root.getSimEntity().listIterator();

	while ( seli.hasNext() ) {

	    SimEntity se = (SimEntity) seli.next();
	    List pl = se.getParameters();
	    ListIterator pli = pl.listIterator();
	    
	    pw.print(sp8 + se.getType() + sp + se.getName() + sp + eq);
	    pw.print(sp + nw + sp + se.getType() + lp);

	    if ( pli.hasNext() ) {
		pw.println();
	        while ( pli.hasNext() ) {
		    doParameter(pl, pli.next(), sp12, pw);
	        }
		pw.println();
	        pw.println(sp8 + rp + sc);
	    } else pw.println(rp + sc);

	    pw.println();

	} 

	pw.println();

    }

     /* Build up a parameter up to but not including a trailing comma. 
      * _callers_ should check the size of the list to determine if a 
      * comma is needed. This may include a closing paren or brace
      * and any nesting. Note a a doParameter may also be a caller
      * of a doParameter, so the comma placement is tricky.
      */
 
    void doParameter(List plist, Object param, String indent, PrintWriter pw) {

	if ( param instanceof MultiParameterType ) {
	    doMultiParameter((MultiParameterType)param, indent, pw);
	} else if ( param instanceof FactoryParameterType ) {
	    doFactoryParameter((FactoryParameterType)param, indent, pw);
	} else { 
	    doTerminalParameter((TerminalParameterType)param, indent, pw);
	}

	maybeComma(plist, param, pw);
    }

    void doFactoryParameter(FactoryParameterType fact, String indent, PrintWriter pw) {
	String factory = fact.getFactory();
	List facts = fact.getParameters();
	ListIterator facti = facts.listIterator();
	pw.println(indent + sp4 + factory + pd + "getInstance" + lp);
	while ( facti.hasNext() ) {
	    doParameter(facts, facti.next(), indent + sp8, pw);
	}
	pw.print(indent + sp4 + rp);
    }

    void doTerminalParameter(TerminalParameterType term, String indent, PrintWriter pw) {

	String type = term.getType();
	String value = term.getValue();
        if ( isPrimitive(type) ) {
	    pw.print(indent + sp4 + value); 
	} else if ( isString(type) ) {
	    pw.print(indent + sp4 + qu + value + qu);
	} else { // some Object
	    pw.print(indent + sp4 + nw + sp + type + lp);
	    pw.print(value + rp);
	}

    }

    void doSimpleStringParameter(TerminalParameterType term, PrintWriter pw) {
	
	String type = term.getType();
	String value = term.getValue();

	if ( isString(type) ) {
	    pw.print(qu + value + qu);
	} else {
	    error("Should only have a single String parameter for this PropertyChangeListener");
	}

    }

    boolean isPrimitive(String type) {
	if (
	    type.equals("boolean") |
	    type.equals("char") |
	    type.equals("double") |
	    type.equals("float") |
	    type.equals("int") |
	    type.equals("long") 
	) return true;
	else return false;
    }

    boolean isString(String type) {
	if ( 
	    type.equals("String") | 
	    type.equals("java.lang.String") 
	) return true;
	else return false;
    }

    boolean isArray(String type) {
	if ( 
	    type.endsWith("[]") |
	    type.endsWith("[ ]") 
	) return true;
	else return false;
    }

    void doMultiParameter(MultiParameterType p, String indent, PrintWriter pw) {

	List params = p.getParameters();
	ListIterator paramsi = params.listIterator();
        String ptype = p.getType();

	if ( isArray(ptype) ) {
	    pw.println(indent + sp4 + nw + sp + ptype + ob);
	    while ( paramsi.hasNext() ) {
		doParameter(params, paramsi.next(), indent + sp4, pw);
	    }
	    pw.print(indent + sp4 + cb);
	} else { // some multi param object
	    pw.println(indent + sp4 + nw + sp + ptype + lp);
	    while ( paramsi.hasNext() ) {
		doParameter(params, paramsi.next(), indent + sp4, pw);
	    }
	    pw.print(indent + sp4 + rp);
	}

    }

    void maybeComma(List params, Object param, PrintWriter pw) {
	if ( params.size() > 1 && params.indexOf(param) < params.size() - 1 ) {
	    pw.println(cm);
	} else pw.println();
    }

    void buildListeners(StringWriter listeners) {
	
	PrintWriter pw = new PrintWriter(listeners);
	ListIterator lili = this.root.getPropertyChangeListener().listIterator();

	while ( lili.hasNext() ) {
	    PropertyChangeListenerType pcl = (PropertyChangeListenerType)lili.next();
	    List tparam = pcl.getTerminalParameter(); 
	    ListIterator tparami = tparam.listIterator();
	    pw.println(sp8 + "java.beans.PropertyChangeListener" + sp + pcl.getName() + sp + eq + sp);
	    pw.print(sp12 + nw + sp + pcl.getType() + lp);
	    if ( tparami.hasNext() ) {
	        while ( tparami.hasNext() ) {
		    // note, check if PropertyChangeListeners can have more than one
		    // String param, if so, do a maybeComma() 
		    doSimpleStringParameter((TerminalParameter)tparami.next(), pw);
	        }
	    } 
	    pw.println(rp + sc);
	    pw.println();
	} 
	
    }

    void buildConnectors(StringWriter connectors) {
	
	PrintWriter pw = new PrintWriter(connectors);
	ListIterator connects = this.root.getSimEventListenerConnection().listIterator();
	
	while ( connects.hasNext() ) {
	    SimEventListenerConnectionType simcon = (SimEventListenerConnectionType)connects.next();
	    pw.print(sp8 + ((SimEntityType)simcon.getSource()).getName() + pd + "addSimEventListener" );
	    pw.println(lp + ((SimEntityType)simcon.getListener()).getName() + rp + sc); 
	}
	pw.println();

	connects = this.root.getPropertyChangeListenerConnection().listIterator();

	while ( connects.hasNext() ) {
	    PropertyChangeListenerConnectionType pccon = (PropertyChangeListenerConnectionType)connects.next();
	    pw.print(sp8 + ((SimEntityType)pccon.getSource()).getName());
	    pw.print(pd + "addPropertyChangeListener" + lp);
	    if ( pccon.getProperty() != null ) {
		pw.print(qu + pccon.getProperty() + qu + cm);
	    } 
	    Object listener = pccon.getListener();
	    if ( listener instanceof SimEntity ) {
	        pw.println(((SimEntityType)(pccon.getListener())).getName() + rp + sc);
	    } else {
	        pw.println(sp + ((PropertyChangeListenerType)(pccon.getListener())).getName() + rp + sc);
	    }
	}
	pw.println();
    }

    void buildOutput(StringWriter out) {
	PrintWriter pw = new PrintWriter(out);
	ListIterator outputs = this.root.getOutput().listIterator();
	while ( outputs.hasNext() ) {
	    SimEntityType entity = (SimEntityType)((OutputType)outputs.next()).getEntity();
	    pw.println(sp8 + "System.out.println" + lp + entity.getName() + rp + sc);
	}
    }

    void buildTail(StringWriter t) {

	PrintWriter pw = new PrintWriter(t);

	pw.println();
	pw.println(cb);
    }

    void buildSource(StringBuffer source, StringWriter head, StringWriter entities, 
		StringWriter listeners, StringWriter connectors, StringWriter output, 
		StringWriter tail ) {
   	 
	source.append(head.getBuffer()).append(entities.getBuffer()).append(listeners.getBuffer());
	source.append(connectors.getBuffer()).append(output.getBuffer()).append(tail.getBuffer());
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

	SimkitAssemblyXML2Java sax2j = new SimkitAssemblyXML2Java(args[0]);
	sax2j.unmarshal();
	String dotJava = sax2j.translate();
	sax2j.writeOut(dotJava,System.out);

    }
    
}
