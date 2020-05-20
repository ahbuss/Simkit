package simkit.examples;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Handler;
import java.util.logging.Logger;
import simkit.random.RandomVariate;
import simkit.util.ClassFinder;

/**
 * <P>
 * Lists examples in the <CODE>simkit.examples</CODE> package along with their
 * Usage statements.
 *
 * @author Arnold Buss
 * 
 *
 */
public class ListExamples {
    
    private static final ClassFinder classFinder = ClassFinder.getINSTANCE();

    /**
     * Print out information about Simkit examples.
     *
     * @param args Given command-line arguments
     */
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(ListExamples.class.getName());
        for (Handler handler: logger.getHandlers()) {
            logger.removeHandler(handler);
        }
        System.out.println(listExamples());
        
        Map<String, Class<? extends RandomVariate>> randomVariates = classFinder.getRandomVariateClasses();
        randomVariates = new TreeMap<>(randomVariates);
        System.out.println("RandomVariate Classes:");
        for (String name: randomVariates.keySet()) {
            System.out.printf("%s => %s%n", name, randomVariates.get(name).getName());
        }
        
    }

    /**
     *
     * @return List of the example Classes.
     */
    public static String listExamples() {
        StringBuilder builder = new StringBuilder("Contents of Package simkit.examples");
        for (String className: classFinder.getFoundByQualifiedName().keySet()) {
            if (className.startsWith("simkit.examples")) {
                Class<?> theClass = classFinder.getFoundByQualifiedName().get(className);
                if (theClass==ListExamples.class) {
                    continue;
                }
                builder.append(System.getProperty("line.separator"));
                builder.append(theClass.getSimpleName());
            }
        }

        return builder.toString();
    }

    /**
     * Returns a String containing a description of this Class.
     *
     * @return A short description of this class
     *
     */
    public static String description() {
        return "Lists all the examples in the simkit.examples package";
    }

    /**
     * Returns a String containing the usage of the main method.
     *
     * @return Usage string.
     *
     */
    public static String usage() {
        return "Usage: java simkit.examples.ListExamples";
    }
}
