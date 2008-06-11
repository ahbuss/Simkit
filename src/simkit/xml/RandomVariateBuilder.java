package simkit.xml;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;


/**
 * Creates instances of RandomVariates from xml Elements.
 * @author  ahbuss
 * @version $Id: RandomVariateBuilder.java 1057 2008-04-09 17:15:14Z kastork $
 */
public class RandomVariateBuilder {
    
/**
* Builds a RandomVariate based on the contents of the given Element.
* @throws IllegalArgumentException If the name of the Element is not "RandomVariate"
**/
    public static RandomVariate buildRandomVariate(Element element) {
        if (!element.getNodeName().equals("RandomVariate")) {
            throw new IllegalArgumentException("Element must be named RandomVariate: " +
                element.getNodeName());
        }
        
        Node classNode = element.getElementsByTagName("class").item(0);
        String className = classNode.getFirstChild().getNodeValue();
        NodeList parameters = element.getElementsByTagName("parameter");
        Object[] params = new Object[parameters.getLength()];
        for (int i = 0; i < params.length; i++) {
            Element parameter = (Element) parameters.item(i);
            String paramClassName = parameter.getAttribute("class");
            try {
                Class<?> paramClass = Thread.currentThread().getContextClassLoader().loadClass(paramClassName);
                Constructor construct = paramClass.getConstructor(new Class[] { java.lang.String.class });
                String arg = parameter.getAttribute("value");
                params[i] = construct.newInstance(new Object[] { arg });
            }
            catch (ClassNotFoundException e) {
                System.err.println(e);
                throw(new RuntimeException(e));
            }
            catch (NoSuchMethodException e) {
                System.err.println(e);
                throw(new RuntimeException(e));
            }
            catch (InstantiationException e) {
                System.err.println(e);
                throw(new RuntimeException(e));
            }
            catch (IllegalAccessException e) {
                System.err.println(e);
                throw(new RuntimeException(e));
            }
            catch (InvocationTargetException e) {
                System.err.println(e.getTargetException());
                throw(new RuntimeException(e));
            }
        }

        RandomVariate variate = RandomVariateFactory.getInstance(className, params);
        Node seedElement = element.getElementsByTagName("seed").item(0);
        if (seedElement != null) {
            String seedString = seedElement.getFirstChild().getNodeValue();
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
        NodeList rvElements = element.getElementsByTagName("RandomVariate");
        RandomVariate[] rv = new RandomVariate[rvElements.getLength()];
        for (int i = 0; i < rv.length; i++) {
            rv[i] = buildRandomVariate((Element) rvElements.item(i));
        }
        return rv;
    }
    
    public static void main(String[] args) throws Throwable {
        String inputFileName = args.length > 0 ? args[0] : "simkit/xml/RandomVariate1.xml";
        java.io.InputStream instream = 
            Thread.currentThread().getContextClassLoader().getResourceAsStream(inputFileName);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(instream);
        instream.close();
        
        RandomVariate[] rv = RandomVariateBuilder.buildRandomVariates(
            document.getDocumentElement()
        );
        
        for (int i = 0; i < rv.length; ++i) {
            System.out.println(rv[i]);
        }
    }
}
