package simkit.data;

public class SamplingType {
   protected SamplingType(String s){ name = s;}
   public static SamplingType TALLY = new SamplingType("TALLY");
   public static SamplingType TIME_VARYING = new SamplingType("TIME_VARYING");
//   public static SamplingType LINEAR = new SamplingType("LINEAR");
   private String name;
   public String toString(){return name;}
}