package simkit.data;

/**
 * @deprecated This class has been replaced by <CODE>simkit.random.BetaVariate</CODE> 
 * @see simkit.random.BetaVariate
**/
public class BetaVariate extends RandomVariateBase {

  private static final double DEFAULT_ALPHA;
  private static final double DEFAULT_BETA;

  static {
    DEFAULT_ALPHA = 1.0;
    DEFAULT_BETA = 1.0;
  }

  private double alpha;
  private double beta;

  private RandomVariate gammaVariate1;
  private RandomVariate gammaVariate2;

  public BetaVariate() {
    this(new Object[] {new Double(DEFAULT_ALPHA), new Double(DEFAULT_BETA)});
  }

  public BetaVariate(Object[] params) {
    this(params, RandomFactory.getRandomNumber());
  }

  public BetaVariate(Object[] params, RandomNumber rng) {
    super(params);
    this.rng = rng;
    setGammas(rng);
  }

  public double generate() {
    double u1 = gammaVariate1.generate();
    double u2 = gammaVariate2.generate();
    return u1 / (u1 + u2);
  }

  public void setParameters(Object[] params) {
     if (params == null) {
       super.setParameters(new Object[]{new Double(DEFAULT_ALPHA), new Double(DEFAULT_BETA)});
     }
     else {
       switch(params.length) {
          case 1:
             super.setParameters(new Object[]{params[0], new Double(DEFAULT_BETA)});
             break;
          case 2:
             super.setParameters(params);
             break;
          default:
            throw new IllegalArgumentException(this.getClass().getName() + " must have two arguments: " + params.length +
                                             " passed");
       }
       setConvenienceParameters();
     }
  }

  private void setConvenienceParameters() {
    alpha = getAlpha();
    beta = getBeta();
  }

  private void setGammas(RandomNumber rng) {
     gammaVariate1 = RandomFactory.getRandomVariate("Gamma", new Object[] {new Double(alpha)}, rng);
     gammaVariate2 = RandomFactory.getRandomVariate("Gamma", new Object[] {new Double(beta)}, rng);
  }

  public double getAlpha() {return ((Number)getParameters()[0]).doubleValue();}
  public double getBeta() {return ((Number)getParameters()[1]).doubleValue();}

} 