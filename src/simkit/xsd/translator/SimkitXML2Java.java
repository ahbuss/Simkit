/*
 * SimkitXML2Java.java
 *
 * Created on March 23, 2004, 4:59 PM
 */

package simkit.xsd.translator;

import java.io.InputStream;
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

    private SimkitModule root;

    InputStream fileInputStream;
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
    String pd  = ".";
    String qu  = "\"";

    
    /** 
     * Creates a new instance of SimkitXML2Java 
     * when used from another class, instance this
     * with a String for the name of the xmlFile
     */

    public SimkitXML2Java(String xmlFile) {
	try {
            jaxbCtx = JAXBContext.newInstance("simkit.xsd.bindings");
	    fileInputStream = Class.forName("simkit.xsd.translator.SimkitXML2Java").getClassLoader().getResourceAsStream(xmlFile);
	} catch ( Exception e ) {
	    e.printStackTrace();
	} 
	
    }
    
    public void unmarshal() {
	Unmarshaller u;
	try {
	    u = jaxbCtx.createUnmarshaller();
	    this.root = (SimkitModule) u.unmarshal(fileInputStream);
	} catch (Exception e) { e.printStackTrace(); }
    }  

    public String translate() {

	StringBuffer source = new StringBuffer();
	StringWriter head = new StringWriter();
	StringWriter tail = new StringWriter();
	StringWriter vars = new StringWriter();
	StringWriter runBlock = new StringWriter();
	StringWriter eventBlock = new StringWriter();
	StringWriter accessorBlock = new StringWriter();

	buildHead(head);
	buildVars(vars, accessorBlock);
	buildEventBlock(runBlock,eventBlock);
	buildTail(tail);

	buildSource(source, head, vars, runBlock, eventBlock, accessorBlock, tail);

	return source.toString();
    }

    void buildHead(StringWriter head) {

	PrintWriter pw = new PrintWriter(head);
	String name = this.root.getName();
	
	pw.println("package examples;");
	pw.println();
	pw.println("import simkit.*;");
	pw.println("import simkit.random.*;");
	pw.println("import java.util.*;");
	pw.println();
	pw.println("public class " + name + sp + "extends SimEntityBase" + sp + ob);
	pw.println();
    }

    void buildVars(StringWriter vars, StringWriter accessorBlock) {

	PrintWriter pw = new PrintWriter(vars);

	ListIterator li = this.root.getParameter().listIterator();

	while ( li.hasNext() ) {

	    Parameter p = (Parameter) li.next();
	    
	    pw.println(sp4 + "private" + sp + p.getType() + sp + p.getName() + sc);

	    buildParameterAccessor(p,accessorBlock);

	} 
	
	pw.println();

	li = this.root.getStateVariable().listIterator();

	while ( li.hasNext() ) {
	    Class c = null;
		
	    StateVariable s = (StateVariable) li.next();

	    try {
		c = Class.forName(s.getType());
	    } catch ( ClassNotFoundException cnfe ) {
	        pw.println(sp4 + "protected" + sp + s.getType() 
			+ sp + s.getName() + sc);
	    }

	    if ( c != null ) {
		pw.println(sp4 + "protected" + sp + s.getType() + sp 
			+ s.getName() + sp + eq + sp + "new" + sp 
			+ s.getType() + lp + rp + sc ) ;	
	    }

	    buildStateVariableAccessor(s,accessorBlock);
	}

    }

    void buildParameterAccessor(Parameter p, StringWriter sw) {

	PrintWriter pw = new PrintWriter(sw);
	
	pw.print(sp4 + "public void set" + capitalize(p.getName()) + lp);	
	pw.print(p.getType() + sp + p.getShortName() + rp + sp + ob + sp);
	pw.println(p.getName() + sp + eq + sp + p.getShortName() + sc + sp + cb);
	pw.print(sp4 + "public " + p.getType() + sp + "get" + capitalize(p.getName()) );
	pw.println(lp + rp + sp + ob + sp + "return" + sp + p.getName() + sc + sp + cb);
	pw.println();
    }

    void buildStateVariableAccessor(StateVariable s, StringWriter sw) {

        PrintWriter pw = new PrintWriter(sw);
	String clStr = "";
	Class aClass = null;

	// check for cloneable 

	try {
	    aClass = Thread.currentThread().getContextClassLoader().loadClass(s.getType());
	} catch ( ClassNotFoundException cnfe ) {
	}
	
	if (aClass != null) {
	    if ( java.lang.Cloneable.class.isAssignableFrom(aClass) ) {
	        clStr = ".clone()";
	    } 
	}

        pw.print(sp4 + "public " + s.getType() + sp + "get" + capitalize(s.getName()) );
        pw.println(lp + rp + sp + ob + sp + "return" + sp + s.getName() + clStr + sc + sp + cb);
        pw.println();
    }

    void buildEventBlock(StringWriter runBlock,StringWriter eventBlock) {
	
	List events = this.root.getEvent();
	ListIterator li = events.listIterator();
	Event e = null;

	while ( li.hasNext() ) {

	    e = (Event) li.next();
	
	    if ( e.getName().equals("Run") ) {
		doRunBlock(e,runBlock);
	    } else {
		doEventBlock(e,eventBlock);
	    }

	}
    }
	
    void doRunBlock(Event run, StringWriter runBlock) {
	
	PrintWriter pw = new PrintWriter(runBlock);
	ListIterator li;
	List sched = run.getScheduleOrCancel();
	ListIterator schi = sched.listIterator();

	pw.println();
	pw.println(sp4 + "/** Creates a new instance of " + this.root.getName() + " */");
	pw.println();
	pw.print(sp4 + "public " + this.root.getName() + lp);

	List pList = this.root.getParameter();
	li = this.root.getParameter().listIterator();

	while ( li.hasNext() ) {

	    Parameter pt = (Parameter) li.next();
		
	    pw.print(pt.getType() + sp + pt.getShortName());
	
	    if ( pList.size() > 1 ) {
 	        if ( pList.indexOf(pt) <  pList.size() - 1 ) {
	            pw.print(cm);
		    pw.println();
		    pw.print(sp8 + sp4);
	        }
	    }
	}

	pw.println(rp + sp + ob);
	pw.println();

	li = this.root.getParameter().listIterator();

	while ( li.hasNext() ) {
	    Parameter pt = (Parameter) li.next();
	    pw.println(sp8 + "set" + capitalize(pt.getName()) + 
		lp + pt.getShortName() + rp + sc);
	}

	pw.println(sp4 + cb);
	pw.println();
	pw.println(sp4 + "/** Set initial values of all state variables */");
	pw.println(sp4 + "public void reset() {");
	pw.println(sp8 + "super.reset()" + sc);
	pw.println();
	pw.println(sp8 + "/** StateTransitions for the Run Event */");
	pw.println();

	li = run.getStateTransition().listIterator();
	
	while ( li.hasNext() ) {
	    StateTransition st = (StateTransition) li.next();
	    StateVariable sv = (StateVariable) st.getState();
	    AssignmentType asg = st.getAssignment();
	    OperationType ops = st.getOperation();
	    pw.print(sp8 + sv.getName());
	    if ( asg == null ) { 
		pw.println( pd + ops.getMethod() + sc );
	    } else {
		pw.println( sp + eq + sp + asg.getValue() + sc );
	    }
	}

	pw.println(sp4 + cb);
	pw.println();
	
	pw.println(sp4 + "public void doRun() {");
	
	li = run.getStateTransition().listIterator();
	
	// because the run event gets split in twain this is 
	// similar as above

	while( li.hasNext() ) {
	    StateTransition st = (StateTransition) li.next();
	    StateVariable sv = (StateVariable) st.getState();
	    AssignmentType asg = st.getAssignment();
	    OperationType ops = st.getOperation();
	    pw.print(sp8 + "firePropertyChange(" + qu + sv.getName() + qu); 
	    pw.println(cm + sv.getName() + rp + sc);
	}

	while ( schi.hasNext() ) {
	    Object o = schi.next();
	    if ( o instanceof ScheduleType ) {
		doSchedule((ScheduleType)o,run,pw);
	    } else {
		doCancel((CancelType)o,run,pw);
	    }
	}
	
	pw.println(sp4 + cb);
	pw.println();
    }

    /** these Events should now not be any Run event */

    void doEventBlock(Event e, StringWriter eventBlock) {
	PrintWriter pw = new PrintWriter(eventBlock);
	ListIterator sli = e.getStateTransition().listIterator();
	List args = e.getArgument();
	ListIterator ai = args.listIterator();
	List locs = e.getLocalVariable();
	ListIterator lci = locs.listIterator();
	List sched = e.getScheduleOrCancel();
	ListIterator schi = sched.listIterator();

	pw.print(sp4 + "public void do" + e.getName() + lp); 

	while ( ai.hasNext() ) {
	    Argument a = (Argument) ai.next();
	    pw.print(a.getType() + sp + a.getName());
	    if ( args.size() > 1 && args.indexOf(a) < args.size() - 1 ) {
		pw.print(cm + sp);
	    }
 	}

	// finish the method decl
	pw.println(rp + sp + ob);

	// local variable decls
	while ( lci.hasNext() ) {
	    LocalVariable local = (LocalVariable) lci.next();
	    pw.print(sp8 + local.getType() + sp + local.getName() + sp + eq);
	    pw.print(sp + lp + local.getType() + rp);
	    pw.println(sp + local.getValue() + sc);
	}
	
	if ( locs.size() > 0 ) pw.println();

	while ( sli.hasNext() ) {
   	    StateTransition st = (StateTransition) sli.next();
	    StateVariable sv = (StateVariable) st.getState();
	    AssignmentType asg = st.getAssignment();
	    OperationType ops = st.getOperation(); 
	
	    pw.print(sp8 + sv.getName());
	    if (asg == null) {
		pw.println(pd + ops.getMethod() + sc);
	    } else {
		pw.println(sp + eq + sp + asg.getValue() + sc);
	    }
	    pw.print(sp8 + "firePropertyChange" + lp + qu + sv.getName() + qu + cm + sp);
	    pw.println(sv.getName() + rp + sc);
	    pw.println();

	}

	// wailDelay/interrupt
	while ( schi.hasNext() ) {
	    Object o = schi.next();
	    if ( o instanceof ScheduleType ) {
		doSchedule((ScheduleType)o,e,pw);
	    } else {
		doCancel((CancelType)o,e,pw);
	    }
	}

	pw.println(sp4 + cb);
	pw.println();
	
    }

    void doSchedule(ScheduleType s, Event e, PrintWriter pw) {
	List edges = s.getEdgeParameter();
	ListIterator ei = edges.listIterator();
	Class c = null;
	String condent = "";
        EventType event = (EventType)s.getEvent();
        List eventArgs = event.getArgument();
        ListIterator eventArgsi = eventArgs.listIterator();
	
	if ( s.getCondition() != null ) {
	    condent = sp4;
	    pw.println(sp8 + "if" + sp + lp + s.getCondition() + rp + sp + ob);
	}	

	pw.print(sp8 + condent + "waitDelay" + lp + qu + ((EventType)s.getEvent()).getName() + qu + cm);
	pw.print(s.getDelay() + cm + "new Object[]" + ob);
	
	while ( ei.hasNext() ) {
	    EdgeParameterType ep = (EdgeParameterType) ei.next();
            ArgumentType arg = (ArgumentType) eventArgsi.next();
	    try {
	        c = Class.forName(arg.getType()); 
	    } catch ( ClassNotFoundException cnfe ) {
		// most likely a primitive type
		String type = arg.getType();
		String constructor = "new" + sp;
		if (type.equals("int")) {
		    constructor+="Integer";
		} else if (type.equals("float")) {
		    constructor+="Float";
		} else if (type.equals("double")) {
		    constructor+="Double";
		} else if (type.equals("long")) {
		    constructor+="Long";
		} else if (type.equals("boolean")) {
		    constructor+="Boolean";
		}
		pw.print(constructor + lp + ep.getValue() + rp);
	    }
	    if (c != null) {
		pw.print(ep.getValue());
	    }
	    
	    if ( edges.size() > 1 && edges.indexOf(ep) < edges.size() - 1 ) {
	        pw.print(cm);
	    }

	}
	pw.println(cb + cm + s.getPriority() + rp + sc);
	
	if ( s.getCondition() != null ) {
	    pw.println(sp8 + cb);
	}
    }

    void doCancel(CancelType c, Event e, PrintWriter pw) {
	List edges = c.getEdgeParameter();
	ListIterator ei = edges.listIterator();
	Class cl = null;
	String condent = "";        
        EventType event = (EventType)c.getEvent();
	List eventArgs = event.getArgument();
        ListIterator eventArgsi = eventArgs.listIterator();

	if ( c.getCondition() != null ) {
	    condent = sp4;
	    pw.println(sp8 + "if" + sp + lp + c.getCondition() + rp + sp + ob);
	}	

	pw.print(sp8 + condent + "interrupt" + lp + qu + event.getName() + qu + cm);
	pw.print("new Object[]" + ob);

	while ( ei.hasNext() ) {
	    EdgeParameterType ep = (EdgeParameterType) ei.next();            
            ArgumentType arg = (ArgumentType) eventArgsi.next();
	    try {
	        cl = Class.forName(arg.getType()); 
	    } catch ( ClassNotFoundException cnfe ) {
		// most likely a primitive type
		String type = arg.getType();
		String constructor = "new" + sp;
		if (type.equals("int")) {
		    constructor+="Integer";
		} else if (type.equals("float")) {
		    constructor+="Float";
		} else if (type.equals("double")) {
		    constructor+="Double";
		} else if (type.equals("long")) {
		    constructor+="Long";
		} else if (type.equals("boolean")) {
		    constructor+="Boolean";
		}
		pw.print(constructor + lp + ep.getValue() + rp);
	    }
	    if (cl != null) {
		pw.print(ep.getValue());
	    }
	    
	    if ( edges.size() > 1 && edges.indexOf(ep) < edges.size() - 1 ) {
	        pw.print(cm);
	    }

	}
	pw.println(cb + rp + sc);
	
	if ( c.getCondition() != null ) {
	    pw.println(sp8 + cb);
	}
	
    }

    void buildTail(StringWriter t) {
	PrintWriter pw = new PrintWriter(t);

	pw.println();
	pw.println(cb);
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
	sx2j.unmarshal();
	String dotJava = sx2j.translate();
	sx2j.writeOut(dotJava,System.out);
    }
    
}
