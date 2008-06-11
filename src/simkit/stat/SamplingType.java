package simkit.stat;
/**
 *  A Java-style "enumeration" class to represent the type of sampling being
 *  done by a given class.  Current options are TALLY and TIME_VARYING.  LINEAR
 *  is not yet implemented.
 *  @author Arnold Buss
 *  @version $Id$
**/
public class SamplingType {

/**
* Creates a new SamplingType with the given name. This constructor
* should only be called during static initialization of this class or
* its children.
**/
   protected SamplingType(String s){ name = s;}

/**
* For statistics that are collected one at a time, independent of time.
**/
   public static SamplingType TALLY = new SamplingType("TALLY");

/**
* For statistics that represent values that vary over time.
**/
   public static SamplingType TIME_VARYING = new SamplingType("TIME_VARYING");

/**
* Not implemented.
**/
   public static SamplingType LINEAR = new SamplingType("LINEAR");

/**
* The name of this SamplingType.
**/
   private String name;

/**
* Returns the name of this SamplingType.
**/
   public String toString(){return name;}
}
