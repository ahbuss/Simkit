package simkit.data;

public class NormalVariate extends RandomVariateBase {

  private static double DEFAULT_MEAN;
  private static double DEFAULT_STD_DEV;

  static {
     DEFAULT_MEAN = 0.0;
     DEFAULT_STD_DEV = 1.0;
  }

  private double savedValue;

  private double mean;
  private double stdDev;

  public NormalVariate() {
    this (new Object[] {new Double(DEFAULT_MEAN), new Double(DEFAULT_STD_DEV)});
  }

  public NormalVariate(RandomNumber rng) {
    this(new Object[] {new Double(DEFAULT_MEAN), new Double(DEFAULT_STD_DEV)}, rng);
  }

  public NormalVariate(RandomNumber rng, long seed) {
    this(new Object[] {new Double(DEFAULT_MEAN), new Double(DEFAULT_STD_DEV)}, rng, seed);
  }

  public NormalVariate(Object[] params) {
    this(params, RandomFactory.getRandomNumber());
  }

  public NormalVariate(Object[] params, long seed) {
    this(params, RandomFactory.getRandomNumber(), seed);
  }
  
  public NormalVariate(Object[] params, RandomNumber rng) {
    this(params, rng, rng.getSeed());
  }

  public NormalVariate(Object[] params, RandomNumber rng, long seed) {
    super(params, rng, seed);
    if (stdDev <= 0 ) {
      throw new IllegalArgumentException("NormalVariate -- Standard Deviation: " +
        stdDev + " (must be > 0)");
    }
    reset();
  }

  public long getSeed() {
    return rng.getSeed();
  }

  public double generate() {
    double generatedValue = Double.NaN;
    if (Double.isNaN(savedValue)) {
      double v1 = Double.NaN;
      double v2 = Double.NaN;
      double w = Double.NaN;
      double y = Double.NaN;
      do {
         v1 = 2.0 * rng.draw() - 1.0;
         v2 = 2.0 * rng.draw() - 1.0;
         w = v1 * v1 + v2 * v2;
      } while (w  > 1.0);
      y = Math.sqrt(- 2.0 * Math.log(w) / w);
      generatedValue = v1 * y;
      savedValue = v2 * y;
    }
    else {
      generatedValue = savedValue;
      savedValue = Double.NaN;
    }
    return generatedValue * stdDev + mean;
  }

  public void reset() {
    savedValue = Double.NaN;
  }

  public void resetSeed() {
    super.resetSeed();
    this.reset();
  }

  public void setParameters(Object[] params) {
    super.setParameters(params);
    Object[] parameters = this.getParameters();
    mean = ((Double)parameters[0]).doubleValue();
    stdDev = ((Double)parameters[1]).doubleValue();
  }

  public static void main(String[] args) {
    double mean = 4.3;
    double std = 3.1;
    RandomStream rs = new RandomStream();
    RandomVariate rv = RandomFactory.getRandomVariate("Normal", mean, std);
    System.out.println(rs.getSeed() +  "\t" + ((NormalVariate) rv).getSeed());

    for (int i = 0; i < 20; i++) {
      System.out.println(rs.boxMuller(mean, std) + "\t" + rv.generate());
    }
    rv = RandomFactory.getRandomVariate("Normal", mean, -std);
    System.out.println(rv);
  }

}
