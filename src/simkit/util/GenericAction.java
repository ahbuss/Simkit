package simkit.util;

import javax.swing.*;
import java.awt.event.*;
import java.lang.reflect.*;

public class GenericAction extends AbstractAction {
   private Object target;
   private Method method;

   public GenericAction (Object theTarget, String methodName) {
      super(methodName);
      target = theTarget;
      try {
         method = target.getClass().getMethod(methodName, null );
      } catch(NoSuchMethodException e) {
           System.err.println(e); e.printStackTrace(System.err);
      }
   }

   public GenericAction (Class theTargetClass, String methodName) {
      super(methodName);
      target = null;
      try {
         method = theTargetClass.getMethod(methodName, null );
      } catch(NoSuchMethodException e) {
           System.err.println(e); e.printStackTrace(System.err);
      }
   }

   public void actionPerformed(ActionEvent event) {
      try {
         method.invoke(target,null);
      } 
      catch( IllegalAccessException e ) {
         System.err.println(e); e.printStackTrace(System.err);
      }
      catch( IllegalArgumentException e ) {
         System.err.println(e); e.printStackTrace(System.err);
      }
      catch( InvocationTargetException e) {
         System.err.println(e.getTargetException());
         e.getTargetException().printStackTrace(System.err);
      }
   }
}
