/*
 * RandomVariateBuilder.java
 *
 * Created on June 26, 2002, 9:16 PM
 */

package simkit.xml;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.jdom.Element;

import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
/**
 * Creates instances of RandomVariates from xml Elements.
 * @author  ahbuss
 * @version $Id$
 */
public class RandomVariateBuilder {
    
/**
* Builds a RandomVariate based on the contents of the given Element.
* @throws IllegalArgumentException If the name of the Element is not "RandomVariate"
**/
    public static RandomVariate buildRandomVariate(Element element) {
        if (!element.getName().equals("RandomVariate")) {
            throw new IllegalArgumentException("Element must be named RandomVariate: " +
                element.getName());
        }
        
        Element classElement = element.getChild("class");
        String className = classElement.getText();
        List parameters = element.getChildren("parameter");
        Object[] params = new Object[parameters.size()];
        for (int i = 0; i < params.length; i++) {
            Element parameter = (Element) parameters.get(i);
            String paramClassName = parameter.getAttribute("class").getValue();
            try {
                Class paramClass = Thread.currentThread().getContextClassLoader().loadClass(paramClassName);
                Constructor construct = paramClass.getConstructor(new Class[] { java.lang.String.class });
                String arg = parameter.getAttribute("value").getValue() ;
                params[i] = construct.newInstance(new Object[] { arg });
            }
            catch (ClassNotFoundException e) { System.err.println(e); }
            catch (NoSuchMethodException e) { System.err.println(e); }
            catch (InstantiationException e) { System.err.println(e); }
            catch (IllegalAccessException e) { System.err.println(e); }
            catch (InvocationTargetException e) { System.err.println(e.getTargetException()); }
        }

        RandomVariate variate = RandomVariateFactory.getInstance(className, params);
        Element seedElement = element.getChild("seed");
        if (seedElement != null) {
            String seedString = seedElement.getText();
            long seed = Long.parseLong(seedString);
            variate.getRandomNumber().setSeed(seed);
        }
        
        return variate;
    }
    
/**
* Builds RandomVariates based on the contents of the child Elements named
* "RandomVariate".
**/
    public static RandomVariate[] buildRandomVariates(Element element) {
        List rvElements = element.getChildren("RandomVariate");
        RandomVariate[] rv = new RandomVariate[rvElements.size()];
        for (int i = 0; i < rv.length; i++) {
            rv[i] = buildRandomVariate((Element) rvElements.get(i));
        }
        return rv;
    }
}
