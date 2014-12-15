package simkit.random;

import java.util.Arrays;

/**
 *
 * @version $Id$
 * @author ahbuss
 */
public class CyclicalStepwiseIntegratedRate{

    private double[] rates;

    private double[] times;

    private double[] cumIntRates;

    /**
     * Rate rates[i] holds in time interval (times[i], times[i+1]).  Thus,
     * there should be one more time than rate.
     * @param rates Constant rates in time intervals; must be &gt;= 0.0
     * @param times Time epochs over which the corresponding rates will hold.
     *          These values must be inclreasing, and times[0] = 0.0.
     */
    public CyclicalStepwiseIntegratedRate(double[] rates, double[] times) {
        this.rates = rates.clone();
        this.times = times.clone();

        if (rates.length == 0) {
            throw new IllegalArgumentException(
                    "Need at least one rate!");
        }

        if (times[0] != 0.0) {
            throw new IllegalArgumentException(
                    "first time must be 0.0! " + times[0]);
        }

        for (int i = 0; i < rates.length; ++i) {
            if (rates[i] < 0.0) {
                throw new IllegalArgumentException(
                        "rates must be non-negative: rates[" +
                        i + "] = " + rates[i]);
            }
        }

        for (int i = 1; i < times.length; ++i) {
            if (times[i] < times[i - 1] ) {
                throw new IllegalArgumentException(
                       "times must be increating: " +
                       "times[" + i + "] = " + times[i] +
                       "times[" + (i - 1) + "] = " + times[i-1]);
            }
        }

        if (rates.length != times.length - 1) {
            throw new IllegalArgumentException(
                    "Need one more time than rate; rates.length = " +
                        rates.length + " times.length = " + times.length);
        }

        cumIntRates = new double[times.length];
        cumIntRates[0] = 0.0;
        for (int i = 1; i < cumIntRates.length; ++i) {
            cumIntRates[i] = cumIntRates[i - 1] + rates[i - 1] * (times[i] - times[i - 1]);
        }

    }

    /**
     * For a given time, returns the corresponding inverse integrated rate for
     * the stepwise rate function. Typically the argument will be that of
     * a unit-rate Poisson process
     *
     * @param y Time to be transformed by the inverse integrated rate.
     * @return Integrated rate for given time y
     */
    public double getLambdaInv(double y) {
        double realTime = Double.NaN;
        double thisTimeInCycle = y % cumIntRates[cumIntRates.length - 1];
        int numberCycles = (int) Math.floor(y / cumIntRates[cumIntRates.length - 1]);
        int cell;
        for (cell = 0; thisTimeInCycle >= cumIntRates[cell + 1]; ++cell) ;

        realTime = times[cell] + (thisTimeInCycle - cumIntRates[cell])/rates[cell];
        realTime += numberCycles * times[cumIntRates.length - 1];
        return realTime ;
    }

    /**
     * These values are the successive stepwise rates that apply throughout
     * a time interval.  rates[i] holds in time interval (times[i], times[i + 1]]
     * <br>Thus there will be one fewer rate than time.
     * @return the rates
     */
    public double[] getRates() {
        return rates;
    }

    /**
     * The time epochs that
     * @return the times
     */
    public double[] getTimes() {
        return times;
    }

    /**
     * These are the cumulative integrated rates.
     * @return the cumIntRates
     */
    public double[] getCumIntRates() {
        return cumIntRates;
    }

    public String paramString() {
        return "times: " + Arrays.toString(times) +
               "\nrates: " + Arrays.toString(rates) +
               "\ncumRates: " + Arrays.toString(cumIntRates);
    }

}
