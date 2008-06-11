package simkit.util;

import javax.swing.*;
import java.awt.event.*;
import java.lang.reflect.*;

/**
* Implements an Action that allows a method to be
* called on a given object when an ActionEvent occurs.
* <P/>The GenericAction can be used as the argument to a 
* JButton constructor to specify a method to execute when the 
* button is pressed. For example:<P><CODE>
* JButton button = new JButton(new GenericAction(this,"exit"));</CODE></P>
* will create a JButton that when called will cause the <CODE>exit()</CODE>
* method of the instance creating the JButton to be called when
* the button is selected.
* <P/>The method specified must be public. If the method doesn't exist
* or is not public, then the program will continue to execute, but the 
* any button associated with this GenericAction will not function.
* @version $Id: GenericAction.java 1057 2008-04-09 17:15:14Z kastork $
**/
public class GenericAction extends AbstractAction {

/**
* The instance whose method will be executed.
**/
   private Object target;

/**
* The name of the method to execute.
**/
   private Method method;

/**
* Creates a new GenericAction for an instance method.
* @param theTarget The instance whose method will be executed.
* @param methodName The name of the method to execute.
**/
   public GenericAction (Object theTarget, String methodName) {
      super(methodName);
      target = theTarget;
      try {
         method = target.getClass().getMethod(methodName, (Class[]) null );
      } catch(NoSuchMethodException e) {
          System.err.println(e);
          e.printStackTrace(System.err);
          throw(new RuntimeException(e));
      }
   }

/**
* Creates a new GenericAction for a class method.
* @param theTargetClass The Class in which the static method exists.
* @param methodName The name of the class method to execute.
**/
   public GenericAction (Class<?> theTargetClass, String methodName) {
      super(methodName);
      target = null;
      try {
         method = theTargetClass.getMethod(methodName, (Class[]) null );
      } catch(NoSuchMethodException e) {
           System.err.println(e);
           e.printStackTrace(System.err);
           throw(new RuntimeException(e));
      }
   }

/**
* Executes the desired method when called back by the 
* Object this GenericAction is registered with.
**/
   public void actionPerformed(ActionEvent event) {
      try {
         method.invoke(target, (Object[])null);
      } 
      catch( IllegalAccessException e ) {
         System.err.println(e); e.printStackTrace(System.err);
         throw(new RuntimeException(e));
      }
      catch( IllegalArgumentException e ) {
         System.err.println(e); e.printStackTrace(System.err);
         throw(new RuntimeException(e));
      }
      catch( InvocationTargetException e) {
         System.err.println(e.getTargetException());
         e.getTargetException().printStackTrace(System.err);
         throw(new RuntimeException(e));
      }
   }
}
