/*
 * SimkitXMLTranslator.java
 *
 * Created on March 23, 2004, 3:02 PM
 */

package simkit.xml;
import org.xml.sax.*;

/**
 *
 * @author  Rick Goldberg
 */
public class SimkitXMLTranslator {
    
    /** Creates a new instance of SimkitXMLTranslator */
    protected SimkitXMLTranslator() {
    }
    
    /**
     * @param args the command line arguments
     * The class reads XML documents according to specified DTD and
     * translates all related events into SimkitHandler events.
     * <p>Usage sample on command line:
     * <pre>
     *    java SimkitXMLTranslator mySimkitModule.xml
     * </pre>
     *
     * To run from Netbeans IDE, select SimkitXMLTranslator.java
     * from the Explorer panel, right click popup menu Properties,
     * select Execution tab, enter name of file to parse in Arguments
     */
    
    public static void main(String[] args) {
        SimkitHandler simkitHandler = new SimkitHandlerImpl();
        EntityResolver entityResolver = null;
        SimkitParser simkitParser = new SimkitParser(simkitHandler, entityResolver);
        InputSource input = new InputSource(args[0]);
        try {
            simkitParser.parse( input );
        } catch (Exception e) { e.printStackTrace(); }
    }
    
}
