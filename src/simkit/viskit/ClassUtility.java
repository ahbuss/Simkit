
package simkit.viskit;

import java.util.HashSet;

/**
 * A utility class that determines whether or not a class
 * exists, given its name. This capability is used in several
 * places so it makes sense to factor this out.
 *
 * @author DMcG
 */
 
 public class ClassUtility extends Object
 {
   private static HashSet resolvedNames = new HashSet();
   
   // Obscure java feature time: a static initializer. This runs once
   // and once only at class load time. In this case it is used to
   // preload the resolved class names hashset with some names
   // that should pass, in this case Java primitives (which are
   // not classes but which we want, in this context, to accept
   // anyway.) Note that the primitive int != the object Integer,
   // and the primitive double != the object Double.

   static
   {
      resolvedNames.add("int");
      resolvedNames.add("double");
      resolvedNames.add("float");
      resolvedNames.add("short");
      resolvedNames.add("byte");
   }
   
   /** negative resolved names are names we have already looked up
    * and not found. Since we know they don't exist, there's no point
    * in looking them up again.
    */
   private static HashSet negativeResolvedNames = new HashSet();
   
   /**
    * Returns true if the class name passed in exists somewhere
    * on the classpath. False otherwise.
    */
   public static boolean classExists(String pClassName)
   {
     // First, see if we've already successfully resolved this class name.
     // If we've done this in the past we don't need to do the expensive
     // process of actually loading it.
     
     if(resolvedNames.contains(pClassName))
       return true;
       
    // Check to see if we've already confirmed this class _isn't_ on
    // the path.
    
    if(negativeResolvedNames.contains(pClassName))
       return false;
       
     // We've never been asked about this class name before. Try to load
     // it; if we throw an exception, we assume it's not on the classpath.
     
     try
     {
       Class.forName(pClassName);
       resolvedNames.add(pClassName);
     }
     catch(ClassNotFoundException cnfe)
     {
       negativeResolvedNames.add(pClassName);
       return false;
     }
     
     return true;
   }
   
 }
   
