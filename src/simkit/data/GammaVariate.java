package simkit.data;

public class GammaVariate extends RandomVariateBase {

  private static final double DEFAULT_ALPHA = 1.0;
  private static final double DEFAULT_BETA = 1.0;

  private double alpha;
  private double beta;

  private double b, p;
  private double a, q, theta, d, z, u1, v, w;
  private double alphaInv;


  public GammaVariate() {
    super(new Object[]{new Double(DEFAULT_ALPHA), new Double(DEFAULT_BETA)});
  }

  public GammaVariate(RandomNumber rng, double alpha, double beta) {
    super(new Object[] {new Double(alpha), new Double(beta)}, rng);
  }

  public GammaVariate(RandomNumber rng, double alpha) {
    super(new Object[] {new Double(alpha), new Double(1.0)}, rng);
  }

  public GammaVariate(Object[] params) {
    super(params);
  }

  public GammaVariate(Object[] params, RandomNumber rng) {
    super(params, rng);
  }

  public GammaVariate(Object[] params, RandomNumber rng, long seed) {
    super(params, rng, seed);
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

  public double generate() {
    double y = 0.0;
      if (alpha < 1.0) {
         while ( true ) {
            p = b * rng.draw();
            if ( p <= 1) {
               y = Math.pow(p, alphaInv);
               if ( rng.draw() <= Math.exp(-y) ) {
                  break;
               }
            } else {
               y = - Math.log( (b - p)/alpha);
               if (rng.draw() <= Math.pow( y, alpha - 1.0) ) {
                  break;
               }
            }
         }
      } else {   
         while (true) {
            u1 = rng.draw();
            v = a * Math.log( u1 / (1.0 - u1) );
            y = alpha * Math.exp(v);
            z = u1 * u1 * rng.draw();
            w = b + q * v - y;
            if (w + d - theta * z >= 0) {
               break;
            } else {
               if (w >= Math.log(z) ) {
                  break ;
               }
            }
         }
       }
       return y * beta;
  }


  private void setConvenienceParameters() {
    alpha = getAlpha();
    beta = getBeta();
    if (alpha < 1.0) {
       b = 1.0 + alpha / Math.E;
       alphaInv = 1.0 / alpha;
    }
    else {
      a = 1.0 / Math.sqrt(2.0 * alpha - 1.0);
      b = alpha - 1.38629436111989061883;    // Number is Math.log(4)
      q = alpha + 1.0 / a;
      theta = 4.5;
      d = 2.504077396776274;        // Math.log(1 + theta)
    }
  }

  public double getAlpha() {return ((Number)getParameters()[0]).doubleValue();}
  public double getBeta() {return ((Number)getParameters()[1]).doubleValue();}
/*
  public static void main(String[] args) {
    Object[] params = new Object[] {new Double(1.5), new Double(2.0)};
    RandomVariate gamma = new GammaVariate(params);
//    RandomStream gamma = new RandomStream();

    DataWindow f = new DataWindow("GammaVariate Random Numbers (?)");
    GraphStat gs = new GraphStat("GammaVariate", 0.0);
    f.add(gs.initHistogram(true, 0.0, 10, 50));

    SimpleStats ss1 = new SimpleStats();

    for(int i = 0; i < 100000; i++) {
      double rand = gamma.generate();
      ss1.newObservation(rand);
//      gs.sample(0.0, rand);
    }
     System.out.println(ss1);

  }
*/
}