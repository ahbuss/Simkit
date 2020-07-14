package simkit.random;

import static java.lang.Double.NaN;
import java.util.logging.Logger;

/**
* Generates a random variate that is the exponential transform of another
* random variate. The generate method simple generates the underlying
* variate, then takes the exponential of it.
* 
* 
**/
public class ExponentialTransform implements RandomVariate {
    
    private static final Logger LOGGER = Logger.getLogger(ExponentialTransform.class.getName());

/**
* An instance of the underlying RandomVariate.
**/
    private RandomVariate rv;

/**
* Constructs a new ExponentialTransform 
**/
    public ExponentialTransform() {
    }

/**
* Generates the next exponentially transformed value.
**/
    public double generate() {
        if (rv == null) {
            LOGGER.severe("RandomVariate not set - will generate NaN");
            return NaN;
        }
        return Math.exp(rv.generate());
    }
    
    /**
     *
     * @param randVar Underlying RandomVariate that will be transformed
     */    
    public void setRandomVariate(RandomVariate randVar) {
        rv = randVar;
    }

    /**
     *
     * @return Underlying RandomVariate instance
     */    
    public RandomVariate getRandomVariate() { return rv; }
    
/**
* Gets the parameters of the underlying RandomVariate.
**/
     public Object[] getParameters() {
         return new Object[] { getRandomVariate() };
     }

/**
* Sets the parameters of the underlying RandomVariate.
**/
    public void setParameters(Object... params) {
        if (params.length != 1) {
            throw new IllegalArgumentException("Need 1 parameter: " + params.length);
        }
        if (! (params[0] instanceof simkit.random.RandomVariate) ) {
            throw new IllegalArgumentException("RandomVariate instance needed: " + params[0].getClass().getName());
        }
        setRandomVariate( (RandomVariate) params[0]);
    }

/**
* Returns the instance of the supporting RandomNumber of the underlying
* RandomVariate.
**/
    public RandomNumber getRandomNumber() { return rv.getRandomNumber(); }

/**
* Sets the supporting RandomNumber of the underlying RandomVariate.
**/
    public void setRandomNumber(RandomNumber rng) { rv.setRandomNumber(rng); }
    
    /**
     * The form is "Exp { RandomVariate's toString() }"
     * @return Description of this RandomVariate with parameters
     */    
    public String toString() {
        return "Exp { " + getRandomVariate() + " }";
    }
}
