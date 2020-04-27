package simkit.xml;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Serializes an Object to xml using javabean Introspection.
 * <br>Note that this Class is not yet complete.
 *
 * @author Arnold Buss
 */
public class Bean2XML {

    /**
     * A single element array containing the String Class.
     *
     */
    public static final Class<?>[] STRING_CLASS_ARRAY = new Class<?>[]{java.lang.String.class};

    private Document document;

    public Bean2XML(String rootElementName) {
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace(System.err);
        }
        document.appendChild(document.createElement(rootElementName));
    }

    /**
     * Adds an Element to the Document containing the Class of the given Object
     * and the name, type, and value of all attributes. Uses java bean
     * Introspection to discover the properties. Note that properties that are
     * references to an Object have shallow values; the value is the Class and a
     * hash value.
     *
     * @param bean Given object
     * @return Element created from given Object
     *
     */
    public Element createElementFromBean(Object bean) {
        Element beanElement = document.createElement("Bean");
        document.getChildNodes().item(0).appendChild(beanElement);
        Element classElement = document.createElement("class");
        classElement.appendChild(document.createTextNode(bean.getClass().getName()));

        try {
            BeanInfo bi = Introspector.getBeanInfo(bean.getClass(), Object.class);
            PropertyDescriptor[] pd = bi.getPropertyDescriptors();
            for (int i = 0; i < pd.length; i++) {
                Method reader = pd[i].getReadMethod();
                if (reader == null) {
                    continue;
                }
                Element propertyElement = document.createElement("property");
                propertyElement.setAttribute("name", pd[i].getName());
                propertyElement.setAttribute("value", reader.invoke(bean, (Object[]) null).toString());
                propertyElement.setAttribute("type", pd[i].getPropertyType().getName());
                beanElement.appendChild(propertyElement);
            }
        } catch (IntrospectionException | IllegalAccessException e) {
            System.err.println(e);
            throw (new RuntimeException(e));
        } catch (InvocationTargetException e) {
            System.err.println(e.getTargetException());
            throw (new RuntimeException(e));
        }

        return beanElement;
    }

    public Document getDocument() {
        return document;
    }

    /**
     * Not yet complete.
     *
     * @param element Given Element
     * @return Object created from given Element
     */
    @SuppressWarnings("unchecked")
    public Object createBeanFromElement(Element element) {
        Object bean = null;
        Node classElement = element.getElementsByTagName("class").item(0);
        if (classElement != null) {
            try {
                Class<?> beanClass = (Class<?>)Thread.currentThread().getContextClassLoader().loadClass(
                        classElement.getNodeValue());
                
                bean = beanClass.getConstructor().newInstance();
                BeanInfo bi = Introspector.getBeanInfo(beanClass, Object.class);
                PropertyDescriptor[] pd = bi.getPropertyDescriptors();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IntrospectionException e) {
                System.err.println(e);
                throw (new RuntimeException(e));
            } catch (NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(Bean2XML.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return bean;
    }

    public static Object createProperty(String name, String value) {
        Object propertyValue = null;
        try {
            Class<?> propertyClass = Thread.currentThread().getContextClassLoader().loadClass(name);
            if (propertyClass.isPrimitive()) {
                propertyClass = getObjectWrapperClassFor(propertyClass);
            }
            Constructor constructor = propertyClass.getConstructor(STRING_CLASS_ARRAY);
            propertyValue = constructor.newInstance(new Object[]{value});
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            System.err.println(e);
            throw (new RuntimeException(e));
        } catch (InvocationTargetException e) {
            System.err.println(e.getTargetException());
            throw (new RuntimeException(e));
        }
        return propertyValue;
    }

    /**
     *
     * @param primitiveClass Given primitive class
     * @return the wrapper Class for the given primitive class.
     */
    public static Class<?> getObjectWrapperClassFor(Class<?> primitiveClass) {
        if (primitiveClass.equals(Integer.TYPE)) {
            return java.lang.Integer.class;
        }
        if (primitiveClass.equals(Double.TYPE)) {
            return java.lang.Double.class;
        }
        if (primitiveClass.equals(Float.TYPE)) {
            return java.lang.Float.class;
        }
        if (primitiveClass.equals(Short.TYPE)) {
            return java.lang.Short.class;
        }
        if (primitiveClass.equals(Character.TYPE)) {
            return java.lang.Character.class;
        }
        if (primitiveClass.equals(Boolean.TYPE)) {
            return java.lang.Boolean.class;
        }
        if (primitiveClass.equals(Byte.TYPE)) {
            return java.lang.Byte.class;
        }
        if (primitiveClass.equals(Void.TYPE)) {
            return java.lang.Void.class;
        }
        return primitiveClass;
    }

    /**
     * Tests the createElementFromBean method.
     *
     * @param args command line arguments
     */
    public static void main(String[] args)  {
        simkit.stat.SimpleStatsTally test = new simkit.stat.SimpleStatsTally("foo");
        simkit.random.RandomNumber rng = simkit.random.RandomNumberFactory.getInstance();
        for (int i = 0; i < 1000; i++) {
            test.newObservation(rng.draw());
        }

        Bean2XML b2x = new Bean2XML("Beans");

        b2x.createElementFromBean(test);
        System.out.println(b2x.getDocument().getDocumentElement());

        test.reset();
        test.setName("bar");
        for (int i = 0; i < 500; i++) {
            test.newObservation(2.0 * rng.draw());
        }

        b2x.createElementFromBean(test);
        System.out.println(b2x.getDocument().getDocumentElement());

        simkit.smd.Mover mover = new simkit.smd.BasicLinearMover("Fred",
                new java.awt.geom.Point2D.Double(20.0, 30.0), 40.0);
        b2x.createElementFromBean(mover);
        System.out.println(b2x.getDocument().getDocumentElement());
    }
}
