package simkit.stat;
/**
 *  A Java-style "enumeration" class to represent the type of sampling being
 *  done by a given class.  Current options are TALLY and TIME_VARYING.  LINEAR
 *  is not yet implemented.
 *  @author Arnold Buss
 *  @version $Date$ 
**/
public class SamplingType {
   protected SamplingType(String s){ name = s;}
   public static SamplingType TALLY = new SamplingType("TALLY");
   public static SamplingType TIME_VARYING = new SamplingType("TIME_VARYING");
   public static SamplingType LINEAR = new SamplingType("LINEAR");
   private String name;
   public String toString(){return name;}
}
