package simkit.data;

/**
 * @deprecated No replacement yet.
**/
public class JavaRandomNumber extends java.util.Random implements RandomNumber{

  private long originalSeed;
  
  public JavaRandomNumber() {
    super();
    originalSeed = this.getSeed();
    this.setSeed(originalSeed);
  }

  public JavaRandomNumber(long seed) {
    super(seed);
    this.setSeed(this.getSeed());
  }

  public void resetSeed() {
     this.setSeed(originalSeed);
  }

  public long getSeed() {
    return this.nextLong();
  }

  public double draw() {
    return this.nextDouble();
  }

  public static void main(String[] args) {
    java.util.Random r = new java.util.Random(12345L);
    r.setSeed(r.nextLong());
    RandomNumber rn = RandomFactory.getRandomNumber("JavaRandomNumber", "simkit.data", 12345L);
    System.out.println(  r.nextDouble());
    System.out.println( rn.draw());

    rn.resetSeed();
    System.out.println( rn.draw());
    rn.resetSeed();
    System.out.println( rn.draw());
    
  }

}
