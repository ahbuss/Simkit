package simkit.test;

/**
 * @version $Id: TestNHPoissonProcessThinned.java 917 2006-10-27 17:56:47Z ahbuss $
 * @author ahbuss
 */
public class TestNHPoissonProcessThinned {
    
    private double firstRate;
    
    private double secondRate;
    
    private double firstRateDuration;
    
    private double secondRateDuration;
    
    private double period;
    
    private double onePeriodValue;
    
    public TestNHPoissonProcessThinned(double firstRate, double secondRate,
            double firstRateDuration, double secondRateDuration) {
        this.firstRate = firstRate;
        this.secondRate = secondRate;
        this.firstRateDuration = firstRateDuration;
        this.secondRateDuration = secondRateDuration;
        
        period = firstRateDuration + secondRateDuration;
        onePeriodValue = firstRate * firstRateDuration + secondRate *
                secondRateDuration;
    }
    
    public double rate(double t) {
        double value = Double.NaN;
        
        int numberPeriods = (int) Math.floor( t / period);
        double reducedTime = t - numberPeriods * period;
        
//        System.out.println("t = " + t + " numberPeriods = " + numberPeriods +
//                " reducedTime = " + reducedTime);
        
        if (reducedTime <= firstRateDuration) {
            value = firstRate;
        }
        else {
            value = secondRate;
        }
        return value;
    }
    
    public double getMaxRate() {
        return Math.max(firstRate, secondRate);
    }
    
    public String toString() {
        return "Two-period constant rate: firstRate = " + firstRate + 
                ", secondRate = " + secondRate +
                ", firstRateDuration = " + firstRateDuration +
                ", secondRateDuration = " + secondRateDuration;
    }
    
    public String getDerivedValues() {
        return "period = " + period + ", onePeriodValue = " + onePeriodValue;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestNHPoissonProcessThinned test = new TestNHPoissonProcessThinned(
                2.0, 4.0, 5.0, 1.0 );
        System.out.println(test);
        System.out.println(test.getDerivedValues());
        
        double[] testTimes = new double[] { 0.0, 1.0, 5.5, 6.0, 9.0, 100000.0,
            10011 * 6.0 + 5.5};
        
        for (int i = 0; i < testTimes.length; ++i) {
            System.out.println("rate(" + testTimes[i] + ") = " + test.rate(testTimes[i]));
        }
            
            
        
    }
    
}
