package simkit.xml;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
/**
 * Serializes an Object to xml using javabean Introspection.
 * <br/>Note that this Class is not yet complete.
 * @author  Arnold Buss
 * @version $Id$
 */
public class Bean2XML {
    
/**
* A single element array containing the String Class.
**/
    public static final Class[] stringClassArray = new Class[] { java.lang.String.class };
    
/**
* Return an XML Element containing the Class of the given Object and the name, type, and
* value of all attributes. Uses java bean Introspection to discover the properties.
* Note that properties that are references to an Object have shallow values; the
* value is the Class and a hash value.
**/
    public static Element createElementFromBean(Object bean) {
        Element beanElement = new Element("Bean");
        Element classElement = new Element("class");
        classElement.setText(bean.getClass().getName());
        
        List children = new ArrayList();
        children.add(classElement);
        try {
            BeanInfo bi = Introspector.getBeanInfo(bean.getClass(), Object.class);
            PropertyDescriptor[] pd = bi.getPropertyDescriptors();
            for (int i = 0; i < pd.length; i++) {
                Method reader = pd[i].getReadMethod();
                if (reader == null) { continue; }
                Element propertyElement = new Element("property");
                propertyElement.setAttribute("name", pd[i].getName());
                propertyElement.setAttribute("value", reader.invoke(bean, null).toString());
                propertyElement.setAttribute("type", pd[i].getPropertyType().getName());
                children.add(propertyElement);
            }
            //beanElement.setChildren(children);
            beanElement.setContent(children);
            
        } 
        catch (IntrospectionException e) {System.err.println(e); }
        catch (IllegalAccessException e) {System.err.println(e); }
        catch (InvocationTargetException e) {System.err.println(e.getTargetException()); }
        
        return beanElement;
    }
    
/**
* Not yet complete.
**/
    public static Object createBeanFromElement(Element element) {
        Object bean = null;
        Element classElement = element.getChild("class");
        if (classElement != null) {
            try {
                Class beanClass = Thread.currentThread().getContextClassLoader().loadClass(
                    classElement.getText());
                bean = beanClass.newInstance();
                BeanInfo bi = Introspector.getBeanInfo(beanClass, Object.class);
                PropertyDescriptor[] pd = bi.getPropertyDescriptors();                
            }
            catch (ClassNotFoundException e) {System.err.println(e);}                
            catch (InstantiationException e) {System.err.println(e); }
            catch (IllegalAccessException e) {System.err.println(e); }
            catch (IntrospectionException e) {System.err.println(e); }
        }
        return bean;
    }
    

    public static Object createProperty(String name, String value) {
        Object propertyValue = null;
        try {
            Class propertyClass = Thread.currentThread().getContextClassLoader().loadClass(name);
            if (propertyClass.isPrimitive()) {
                propertyClass = getObjectWrapperClassFor(propertyClass);
            }
            Constructor constructor = propertyClass.getConstructor(stringClassArray);
            propertyValue = constructor.newInstance(new Object[] { value } );
        } 
        catch (ClassNotFoundException e) { System.err.println(e); }
        catch (NoSuchMethodException e) { System.err.println(e); }
        catch (InstantiationException e) {System.err.println(e); }
        catch (IllegalAccessException e) {System.err.println(e); }
        catch (InvocationTargetException e) {System.err.println(e.getTargetException()); }
        return propertyValue;
    }
    
/**
* Returns the wrapper Class for the given primative.
**/
    public static Class getObjectWrapperClassFor(Class primitiveClass) {
        if (primitiveClass.equals(Integer.TYPE)) { return java.lang.Integer.class; }
        if (primitiveClass.equals(Double.TYPE)) { return java.lang.Double.class; }
        if (primitiveClass.equals(Float.TYPE)) { return java.lang.Float.class; }
        if (primitiveClass.equals(Short.TYPE)) { return java.lang.Short.class; }
        if (primitiveClass.equals(Character.TYPE)) { return java.lang.Character.class; }
        if (primitiveClass.equals(Boolean.TYPE)) { return java.lang.Boolean.class; }
        if (primitiveClass.equals(Byte.TYPE)) { return java.lang.Byte.class; }
        if (primitiveClass.equals(Void.TYPE)) { return java.lang.Void.class; }
        return primitiveClass;
    }
    
/**
* Tests the createElementFromBean method.
**/
    public static void main(String[] args) throws Throwable {
        simkit.stat.SimpleStatsTally test = new simkit.stat.SimpleStatsTally("foo");
        simkit.random.RandomNumber rng = simkit.random.RandomNumberFactory.getInstance();
        for (int i = 0; i < 1000; i++) {
            test.newObservation(rng.draw());
        }
        Element root = new Element("Stats");
        Element element = createElementFromBean(test);
        root.addContent(element);
        
        test.reset();
        test.setName("bar");
        for (int i = 0; i < 500; i++) {
            test.newObservation(2.0 * rng.draw());
        }
        root.addContent(createElementFromBean(test));
        
        simkit.smdx.Mover mover = new simkit.smdx.UniformLinearMover("Fred",  
            new java.awt.geom.Point2D.Double(20.0, 30.0), 40.0);
        root.addContent(createElementFromBean(mover));
        
        root.addContent(createElementFromBean(mover.getLocation()));
        
        Document doc = new Document(root);
        XMLOutputter outputter = new XMLOutputter();
        outputter.setNewlines(true);
        outputter.setIndent("  ");
        outputter.output(doc, System.out);
        
    }
}
