package simkit.random;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import simkit.BasicSimEntity;

/**
 * @version $Id: NPPoissonProcessThinned.java 1010 2007-03-16 18:38:16Z ahbuss $;
 * @author ahbuss
 */
public class NPPoissonProcessThinned extends BasicSimEntity implements RandomVariate {
    
    private double lambda;
    
    private RandomNumber rng;
    
    private Object rateInvoker;
    
    private Method rateMethod;
    
    protected double startTime;
    
    protected double lastTime;
    
    public NPPoissonProcessThinned() {
        setRandomNumber(RandomVariateFactory.getDefaultRandomNumber());
    }

    public void reset() {
        super.reset();
        startTime = getEventList().getSimTime();
        lastTime = getStartTime();
    }
    
    public double generate() {
        double generatedTime = Double.NaN;
        
        double absoluteTime = Double.NaN;
        double lambdaT = Double.NaN;
        double t;
        do {
            t = getLastTime() - 1.0 / getLambda() * rng.draw();
            Number num = null;
            try {
                num = (Number) rateMethod.invoke(rateInvoker, new Object[] { new Double(t) } );
            }
            catch (IllegalAccessException e) { throw new RuntimeException(e);}
            catch (InvocationTargetException e) { throw new RuntimeException(e.getTargetException());}
            lambdaT = num.doubleValue();
        } while (getLambda() * rng.draw() > lambdaT);
        generatedTime = t - getLastTime();
        lastTime = t;
        return generatedTime;
    }

    public Object[] getParameters() {
        return new Object[] {
            new Double(lambda),
            rateInvoker,
            rateMethod
        };
    }

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
                        new Class[] { double.class } ));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            throw new RuntimeException("Third argument must be Method or String");
        }
        
    }
    
    public double getLambda() {
        return lambda;
    }

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

    public Object getRateInvoker() {
        return rateInvoker;
    }

    public void setRateInvoker(Object rateInvoker) {
        this.rateInvoker = rateInvoker;
    }

    public Method getRateMethod() {
        return rateMethod;
    }

    public void setRateMethod(Method rateMethod) {
        this.rateMethod = rateMethod;
    }

    public double getStartTime() {
        return startTime;
    }

    public double getLastTime() {
        return lastTime;
    }
    
    public String toString() {
        return "Non-Homogeneous Poisson Process by Thinning (" +
                getRateInvoker() + ", " + getRateMethod();
    }
    
    public void handleSimEvent(simkit.SimEvent event) {
    }

    public void processSimEvent(simkit.SimEvent event) {
    }

}