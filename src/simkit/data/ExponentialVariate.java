package simkit.data;

public class ExponentialVariate extends RandomVariateBase implements RandomVariate {

  private double mean;
  public final static double DEFAULT_MEAN = 1.0;

  public ExponentialVariate(Object[] params, RandomNumber rng, long seed) {
    super(params, rng, seed);
  }

  public ExponentialVariate(Object[] params, long seed) {
    super(params, seed);
  }

  public ExponentialVariate(Object[] params, RandomNumber rng) {
    super(params, rng, rng.getSeed());
  }

  public ExponentialVariate(Object[] params) {
    super(params);
    if (params.length == 0) {
      this.setParameters(new Object[] {new Double(DEFAULT_MEAN)});
    }
  }

  public ExponentialVariate() {
    this(new Object[] {new Double(DEFAULT_MEAN)});
  }

  public ExponentialVariate(double mean, long seed) {
    this(new Object[] {new Double(mean)}, seed);
  }

  public ExponentialVariate(double mean) {
    this(new Object[] {new Double(mean)});
  }

  public double generate() {
    return - mean * Math.log(rng.draw());
  }

  public void setParameters(Object[] params) {
    if (params.length != 1) {
      throw new IllegalArgumentException("Should be only one parameter for Exponential: " +
           params.length + " passed.");
    }
    double temp = ((Number) params[0]).doubleValue();
    if (temp > 0) {
      super.setParameters(params);
      this.mean = ((Number) getParameters()[0]).doubleValue();
    }
    else {
      throw new IllegalArgumentException("Exponential mean must be positive: " + mean);
    }
  }

  public static void main(String[] args) {
    RandomVariate rv = RandomFactory.getRandomVariate("Exponential", 1.5);
    for (int i = 0; i < 10; i++) {
      System.out.println(rv.generate());
    }
  }

 
} 