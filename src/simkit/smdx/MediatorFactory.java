/*
 * Mediator.java
 *
 * Created on March 6, 2002, 9:09 PM
 */

package simkit.smdx;
import java.util.*;
/**
 *
 * @author  Arnold Buss
 * @version 
 */
public interface MediatorFactory {
    
    public Map getMediators();
    
    public void addMediatorFor(Class first, Class second, Class mediator);
    
    public void addMediatorFor(String first, String second, String mediator)
        throws ClassNotFoundException;
    
    public Mediator getMeditorFor(Object first, Object second);
    
    public Mediator getMediatorFor(Class firstClass, Class secondClass);
    
    public void clear();

}

