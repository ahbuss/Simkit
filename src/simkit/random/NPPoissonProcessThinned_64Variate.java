package simkit.random;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import simkit.BasicSimEntity;
import simkit.util.Math64;

/** Generates a nonhomogeneous Poisson process variate using the method of
 *  thinning by Lewis and Shedler.  A nonhomogeneous Poisson process has a rate
 *  function that varies over time.  See Law and Kelton, <em>Simulation Modeling
 *  and Analysis, Third Edition</em>, page 486 for the algorithm.  The user needs
 *  to have three parameters for this random variate:
 * <ol>
 * <li>lambda - The maximum value of the rate function over the time span of the process.</li>
 * <li>rateInvoker - An object that contains a method to evaluate the rate function.</li>
 * <li>rateMethod - The name of the method that represents the rate function within the rateInvoker object.</li>
 * </ol>
 * Before the generate() method can be called, these three parameters must be set
 * using one of the methods provided.
 *
 * <P>Uses simkit.util.Math64.log() function for replicability on 64-bit
 * platforms.
 * ;
 * @author ahbuss
 */
public class NPPoissonProcessThinned_64Variate extends BasicSimEntity implements RandomVariate {
    
    private double lambda;
    
    private RandomNumber rng;
    
    private Object rateInvoker;
    
    private Method rateMethod;
    
    /**
     *  The starting time for the process.
     */
    protected double startTime;
    
    /**
     *  The previous time returned by generate().
     */
    protected double lastTime;
    
    /**
     *  Constructor to create an object without the parameters set.
     */
    public NPPoissonProcessThinned_64Variate() {
        setRandomNumber(RandomVariateFactory.getDefaultRandomNumber());
    }

    /**
     * Rests startTime and lastTime to the starting simulation time.  This
     * will most often be 0.0.  Note that this will be invoked from the
     * Event List and does not need to be explicitly called.
     */
    public void reset() {
        super.reset();
        startTime = getEventList().getSimTime();
        lastTime = getStartTime();
    }

    /**
     * This uses the thinning algorithm of Lewis and Shedler.  A time, t, is
     * first generated based on a homogeneous Poisson process with the
     * maximum rate &lambda;*; (<code>lambda</code> here)
     * (i.e. a rate that is greater than any rate in the non-
     * homogeneous PP).  It is then accepted with probability
     * &lambda;(t);/&lambda;*; - here <code>lambdaT</code> is &lambda;(t);.
     * @return The next Non-homogeneous Poisson time based on the given rate
     */
    public double generate() {
        double generatedTime = Double.NaN;        
//      double absoluteTime = Double.NaN;   Unused - Removed by A. Seila 1/22/2009
        double lambdaT = Double.NaN;
        double t = getLastTime();  // Modified by A. Seila 1/22/2009
        do {
            // added Math64.log  - A. Seila 1/20/2009
            // Changed getLastTime() to t  - A. Seila 1/22/2009
            t = t - 1.0 / getLambda() * Math64.log(rng.draw());
            Number num = null;
            try {
                num = (Number) rateMethod.invoke(rateInvoker, new Object[] { t } );
            }
            catch (IllegalAccessException e) { throw new RuntimeException(e);}
            catch (InvocationTargetException e) { throw new RuntimeException(e.getTargetException());}
            lambdaT = num.doubleValue();
        } while (getLambda() * rng.draw() > lambdaT); 
        generatedTime = t - getLastTime();
        lastTime = t;
        return generatedTime;
    }

    /**
     * Get the three parameters of this process: lambda (max rate, double),
     * rateInvoker (Object), and rateMethod (String)
     * @return Array of three objects with the three parameters.
     */
    public Object[] getParameters() {
        return new Object[] {
            lambda,
            rateInvoker,
            rateMethod
        };
    }

    /**
     * Set the three parameters of this process: lambda (max rate, double),
     * rateInvoker (Object), and rateMethod (String)
     * @param obj Array of three objects with the three parameters.
     */
    public void setParameters(Object... obj) {
        if (obj.length != 3) {
            throw new IllegalArgumentException(
                "NHPoissonProcessVariate requires 3 parameters: " +
                "(lambda, <rate object>, <rate method>)");
        }
        if (obj[0] instanceof Number) {
            setLambda(((Number) obj[0]).doubleValue());
        } else {
            throw new IllegalArgumentException("First parameter must be a Number");
        }
        setRateInvoker(obj[1]);
        if (obj[2] instanceof Method) {
            setRateMethod((Method) obj[2]);
        }
        else if (obj[2] instanceof String) {
            try {
                Class<?> clazz = obj[1].getClass();
                setRateMethod(clazz.getMethod(obj[2].toString(), 
                        new Class<?>[] { double.class } ));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            throw new RuntimeException("Third argument must be Method or String");
        }
        
    }
    
    /**
     * Get the first parameter - the maximum value of the rate function over the
     * time span of the process.
     * @return The maximum rate over the time interval of the process.
     */
    public double getLambda() {
        return lambda;
    }

    /**
     * Set the first parameter - the maximum value of the rate function over the
     * time span of the process.
     * @param lambda The maximum value of the rate function.
     */
    public void setLambda(double lambda) {
        if (lambda <= 0.0) {
            throw new IllegalArgumentException("Majorizing Rate must be > 0.0: " +
                    lambda);
        }
        this.lambda = lambda;
    }

    public RandomNumber getRandomNumber() {
        return rng;
    }

    public void setRandomNumber(RandomNumber rng) {
        this.rng = rng;
    }

    /**
     * Get the second parameter - the object containing the method which computes
     * the rate function.
     * @return rateInvoker - The object used to invoke the rate function.
     */
    public Object getRateInvoker() {
        return rateInvoker;
    }

    /**
     * Set the second parameter - the object containing the method which computes
     * the rate function.
     * @param rateInvoker The object used to invoke the rate function.
     */
    public void setRateInvoker(Object rateInvoker) {
        this.rateInvoker = rateInvoker;
    }

    /**
     * Get the method that computes the rate function within rateInvoker
     * @return The method to compute the rate function.
     */
    public Method getRateMethod() {
        return rateMethod;
    }

    /**
     * set the method that computes the rate function within rateInvoker
     * @param rateMethod The method that computes the rate function.
     */
    public void setRateMethod(Method rateMethod) {
        this.rateMethod = rateMethod;
    }

    /**
     * Get the starting time for the process.
     * @return The starting time.
     */
    public double getStartTime() {
        return startTime;
    }

    /**
     * Get the previous time that was generated by the process.
     * @return The previously generated time.
     */
    public double getLastTime() {
        return lastTime;
    }
    
    public String toString() {
        return "Non-Homogeneous Poisson Process_64 by Thinning (" +
                getRateInvoker() + ", " + getRateMethod();
    }
    
    public void handleSimEvent(simkit.SimEvent event) {
    }

    public void processSimEvent(simkit.SimEvent event) {
    }

}