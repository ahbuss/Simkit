package simkit.data;
/**
 * @deprecated This class has been replaced by <CODE>simkit.stat.SamplingType</CODE>
 * @see simkit.stat.SamplingType
**/
public class SamplingType {
   protected SamplingType(String s){ name = s;}
   public static SamplingType TALLY = new SamplingType("TALLY");
   public static SamplingType TIME_VARYING = new SamplingType("TIME_VARYING");
//   public static SamplingType LINEAR = new SamplingType("LINEAR");
   private String name;
   public String toString(){return name;}
}