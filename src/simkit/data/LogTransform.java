package simkit.data;

public class LogTransform  implements RandomVariate {

  private RandomVariate rv;

  public LogTransform(RandomVariate rv, long seed) {
    this(rv);
    rv.setSeed(seed);
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

  public void setSeed(long seed) {rv.setSeed(seed);}
  public void resetSeed() {rv.resetSeed();}

  public long getSeed() { return rv.getSeed(); }

  public Object[] getParameters() {return rv.getParameters();}

  public void setParameters(Object[] params) {rv.setParameters(params);}
}
