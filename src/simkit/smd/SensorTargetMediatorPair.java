package simkit.smd;

/**
 *  <P> 08 March 1999 -- Added to simkit.smd package
**/

import java.lang.reflect.*;

public class SensorTargetMediatorPair {
  private Constructor mediatorConstructor;
  private Class sensorClass = null;
  private Class targetClass = null;

  public SensorTargetMediatorPair(String sensorName, String targetName,
                                  String mediatorName) {
      Class mediatorClass = null;
      try {
          sensorClass = Class.forName(sensorName);
          targetClass = Class.forName(targetName);
          mediatorClass = Class.forName(mediatorName);

          mediatorConstructor = mediatorClass.getConstructor(
            new Class[] {sensorClass, targetClass});
      }
      catch (NoSuchMethodException e) {System.err.println(e); e.printStackTrace(System.err);}
      catch (ClassNotFoundException e) {System.err.println(e); e.printStackTrace(System.err);}
  }

  public Constructor getMediatorConstructor() {return mediatorConstructor;}
  public Class getSensorClass() {return sensorClass;}
  public Class getTargetClass() {return targetClass;}
}
