package simkit.random;

public class LogTransform  implements RandomVariate {

  private RandomVariate rv;

  public LogTransform(RandomVariate rv, long seed) {
    this(rv);
    rv.getRandomNumber().setSeed(seed);
  }

  public LogTransform(RandomVariate rv) {
    this.rv = rv;
  }

  public double generate() {
    double original = rv.generate();
    if (original > 0) {
      return Math.log(original);
    }
    else {
      return Double.NaN;
    }
  }

  public void setSeed(long seed) {rv.getRandomNumber().setSeed(seed);}
  public void resetSeed() {rv.getRandomNumber().resetSeed();}

  public long getSeed() { return rv.getRandomNumber().getSeed(); }

  public Object[] getParameters() {return rv.getParameters();}

  public void setParameters(Object[] params) {rv.setParameters(params);}

  public RandomNumber getRandomNumber() { return rv.getRandomNumber(); }

  public void setRandomNumber(RandomNumber rng) { rv.setRandomNumber(rng); }
  
  public Object clone() {
      try {
          return super.clone();
      } catch(CloneNotSupportedException e) {}
      finally{}
      return null;
      
  }
}
