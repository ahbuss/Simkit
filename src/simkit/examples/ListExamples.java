package simkit.examples;
import java.lang.reflect.Method;
/**
 *  <P> Lists examples in the <CODE>simkit.examples</CODE> package along with their
 *  Usage statements.
 *  @author Arnold Buss
**/
public class ListExamples {

    public static final String[] EXAMPLES =
        {
            "ListExamples",
            "ArrivalProcess",
            "MultipleServerQueue"
        };    

    public static final String EXAMPLES_PACKAGE = "simkit.examples";

    public static final String DEFAULT_DESCRIPTION = "No description available";

    public static final String DEFAULT_USAGE = "No usage statement available";

    public static String EOL = System.getProperty("line.separator", "\n");

    public static void main(String[] args) throws Throwable {
        System.out.println(listExamples());
    }

    public static String listExamples() throws Throwable{
        StringBuffer buf = new StringBuffer("Contents of Package simkit.examples");
        buf.append(EOL);
        buf.append(EOL);
        Method description = null;
        Method usage = null;
        for (int i = 0; i < EXAMPLES.length; i++) {
            buf.append(i);
            buf.append(". ");
            buf.append(EXAMPLES[i]);
            buf.append(": ");
            try {
                description =
                    Thread.currentThread().getContextClassLoader().loadClass(
                        EXAMPLES_PACKAGE + '.' + EXAMPLES[i]).getMethod("description", null);
                buf.append(description.invoke(null, null) );
            }
            catch (ClassNotFoundException e) {
                System.err.println(e);
                buf.append("Not found");
                buf.append(EOL);
                buf.append(EOL);
                continue;
            }
            catch (NoSuchMethodException e) {
                        buf.append(DEFAULT_DESCRIPTION);
            }
            buf.append(EOL);
            try {
                usage =
                    Thread.currentThread().getContextClassLoader().loadClass(EXAMPLES_PACKAGE + '.' + EXAMPLES[i]).getMethod("usage", null);
                buf.append('\t');
                buf.append( usage.invoke(null, null) );
            }
            catch (NoSuchMethodException e) {
                buf.append('\t');
                buf.append( DEFAULT_USAGE);
            }
            if (i ==  EXAMPLES.length - 1) { break; }
            buf.append(EOL);
            buf.append(EOL);
        }
        return buf.toString();
    }
/**
 *  @return A short description of this class
**/
    public static String description() {
        return "Lists all the examples in the simkit.examples package";
    }
/**
 *  @return Usage string.
**/
    public static String usage() {
        return "Usage: java simkit.examples.ListExamples";
    }
} 