package simkit.smd;

/**
 *  A "factory" for creating mediators.  Currently the only kind is a
 *  Cookie-cutter; subclasses should be capable of creating more detailed
 *  mediators.
 *
 * <UL> 
 * <LI> 23 Feb 1999 - Added to smd package
 * <LI> 08 March 1999 - "Improved" and more flexible way of adding new mediators
 * <LI> 18 August 1999 - fixed bug in addMediatorType that overwrote previously
 *      added mediator constructors.
 * </UL> 
 *
 * @author Arnold Buss
 * @version 1.0.10
**/

import java.util.*;
import java.lang.reflect.*;
import simkit.util.*;

public class MediatorFactory {

    private static boolean verbose;
    public static boolean isVerbose() {return verbose;}
    public static void setVerbose(boolean v) {verbose = v;}

//    private static Hashtable mediators;
//    private static Hashtable createdMediators;
    private static Hashtable2 mediators;
    private static Hashtable2 createdMediators;

    static {
        mediators = new Hashtable2();
        createdMediators = new Hashtable2();
    }


  public static Mediator getMediator(Mover target, Sensor sensor) {
    Mediator mediator  = (Mediator) createdMediators.get(sensor, target);  
    if (mediator != null) {
        if (verbose) {
            System.out.println("Cached mediator " + mediator + " found for " + sensor + " / " + target);
        }
    }
    else {
        if (verbose) {
            System.out.println("Getting mediator for (" + sensor.getClass() + ", " + target.getClass() + ")");
        }
        try {
           Constructor medConstructor = (Constructor) mediators.get(sensor.getClass(), target.getClass());
           if (verbose) {
              System.out.println("For (" + sensor.getClass() + ", " + target.getClass() + "): " +
                medConstructor);
           }
           mediator = (Mediator) medConstructor.newInstance(
                new Object[] {sensor, target});

            if (verbose) {
                System.out.println("Created mediator " + mediator + " for " + sensor + " / " + target);
            }

           createdMediators.put(sensor, target, mediator);
        }

        catch (IllegalAccessException e) {System.err.println(e); e.printStackTrace(System.err);}
        catch (InstantiationException e) {System.err.println(e); e.printStackTrace(System.err);}

        catch (InvocationTargetException e) {
            System.err.println(e.getTargetException());
            e.printStackTrace(System.err);
        }
        if (verbose) {
            System.out.println("Mediator " + mediator + " created for sensor " +
               sensor + " and target " + target);
        }

    }

    return mediator;
  }

  public static void addMediatorType(String targetClassName, String sensorClassName, 
                                 String mediatorClassName) {
      try {
          Class targetClass = Class.forName(targetClassName);
          Class sensorClass = Class.forName(sensorClassName);
          Class mediatorClass = Class.forName(mediatorClassName);

          Constructor mediatorConstructor = mediatorClass.getConstructor(
            new Class[] {simkit.smd.Sensor.class, simkit.smd.Mover.class});
          mediators.put(sensorClass, targetClass, mediatorConstructor);
/*
          Hashtable tgt = (Hashtable) mediators.get(sensorClass);
          if (tgt == null) { tgt = new Hashtable(10);}
          tgt.put(targetClass, mediatorConstructor);
          mediators.put(sensorClass, tgt);
*/
          if (verbose ) {
             System.out.println("Added mediator constructor " + mediatorConstructor +
                " for (" + sensorClass + ", " + targetClass + ")"); 
          }
      }
      catch (NoSuchMethodException e) {System.err.println(e); e.printStackTrace(System.err);}
      catch (ClassNotFoundException e) {System.err.println(e); e.printStackTrace(System.err);}
  }

    public static String dumpMediatorTypes() {
        return mediators.toString();
    }

  /*
  public static void addMediatorType(String targetClassName, String sensorClassName,
                                 String mediatorClassName, Object[] parameters) {
      Class sensorClass = null;
      Class targetClass = null;
      Class mediatorClass = null;
      Constructor mediatorConstructor = null;
      try {
          sensorClass = Class.forName(sensorClassName);
          targetClass = Class.forName(targetClassName);
          mediatorClass = Class.forName(mediatorClassName);

          mediatorConstructor = mediatorClass.getConstructor(
            new Class[] {simkit.smd.Sensor.class, simkit.smd.Mover.class, java.lang.Object[].class});
          Hashtable tgt = new Hashtable(2);
          tgt.put(targetClass, mediatorConstructor);
          tgt.put("parameters", parameters);
          mediators.put(sensorClass, tgt);
      }
      catch (NoSuchMethodException e) {System.err.println(e); e.printStackTrace(System.err);}
      catch (ClassNotFoundException e) {System.err.println(e); e.printStackTrace(System.err);}
  }
*/
}




