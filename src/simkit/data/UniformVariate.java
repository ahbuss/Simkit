package simkit.data;
/**
 * @deprecated This class has been replaced by <CODE>simkit.random.UniformVariate<CODE>
 * @see simkit.random.UniformVariate
**/
public class UniformVariate implements RandomVariate {

  private static double DEFAULT_LOW;
  private static double DEFAULT_HIGH;

  private RandomNumber rng;
  private double low;
  private double high;
  private double range; 

  static {
    DEFAULT_LOW = 0.0;
    DEFAULT_HIGH = 1.0;
  }

  public UniformVariate() {
  }

  public UniformVariate(Object[] params, long seed, RandomNumber rng) {
    this.rng = rng;
    rng.setSeed(seed);
    this.setParameters(params);
  }

  public UniformVariate(Object[] params, long seed) {
    this(params, seed, RandomFactory.getRandomNumber(seed));
  }

  public UniformVariate(Object[] params, RandomNumber rng) {
    this(params, rng.getSeed(), rng);
  }

  public UniformVariate(Object[] params) {
    rng = RandomFactory.getRandomNumber();
    if (params.length != 2) {
      throw new IllegalArgumentException("Uniform parameters must have two values (" +
        params.length + " passed.");
    }
    else {
      this.setParameters(params);
    }
  }

  public UniformVariate(double low, double high, long seed) {
    this(new Object[] {new Double(low), new Double(high)}, seed);
  }

  public UniformVariate(double low, double high) {
    this(new Object[] {new Double(low), new Double(high)});
  }

  public void setParameters(Object[] params) {
    double newLow = ((Number)params[0]).doubleValue();
    double newHigh = ((Number)params[1]).doubleValue();

    if (newLow > newHigh) {
      throw new IllegalArgumentException("Uniform parameters must have low < high: (" +
                newLow + ", " + newHigh + ")");
    }
    this.low = newLow;
    this.high = newHigh;
    range = high - low;
  }

  public Object[] getParameters() {
    return new Object[] {new Double(low), new Double(high)};
  }

  public void setSeed(long newSeed) {rng.setSeed(newSeed);}
  public void resetSeed() {rng.resetSeed();}

  public long getSeed() { return rng.getSeed(); }

  public double generate() {
    return low + range * rng.draw();
  }

/*
  public static void main(String[] args) {

     double low = -2;
     double high = 3;

     DataWindow f = new DataWindow("Uniform Variate Test");
     f.setLayout(new java.awt.GridLayout(0, 3));
     f.setBounds(100, 100, 400, 400);
     f.setVisible(true);


     GraphStat[] graphStat = new GraphStat[1];
     graphStat[0] = new GraphStat("Unifom[0]", 0.0);
     // f.add(graphStat[0].initHistogram(true, low, high, 50));

     RandomVariate uniform = new UniformVariate(low, high);
     SimpleStats s = new SimpleStats();
     for (int i = 0; i < 10000; i++) {
//       graphStat[0].sample(0.0, uniform.generate());

         s.newObservation(uniform.generate());
     }

     System.out.println(s);

  }
*/
}