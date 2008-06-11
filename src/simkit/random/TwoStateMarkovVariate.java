package simkit.random;

/**
 *
 * <p>Generates values from a 2-D Markov Chain taking on values {0,1}.
 * The parameters the probabilities that a transition will <em>not</em>
 * be made (i.e. that the next state will be identical to the first).
 * So p[0] = Pr{X1 = 0 | X0 = 0} and p[1] = Pr{X1 = 1 | X0 = 1}.
 * A double[] array should be passed in the Object[] params.  An optional
 * second value in params is a Number instance representing the initial state.
 * This should be 0 or 1.  The setters check validity of the arguments.
 * @author  Arnold Buss
 * @version $Id$
 */
public class TwoStateMarkovVariate extends RandomVariateBase implements DiscreteRandomVariate {
    
/**
* A two element array holding the transition probabilities.
**/
    private double[] transitionProb;

/**
* The state of the system last time it was set.
**/
    private int initialState;

/**
* The current state of the system 
**/
    protected int currentState;
    
/**
* Makes a new TwoStateMarkovVariate with initialState 0 and no transition probabilities.
* The transition probabilities must be set prior to use.
**/
    public TwoStateMarkovVariate() {
    }
    
    /** 
     * Generate the next state of the system 
     * @return The state of the system (0 or 1) cast to a double 
     */
    public double generate() {
        return (double) generateInt();
    }
    
    /**
     * Generate the next state of the system
     * @return The state of the system (0 or 1).
     */    
    public int generateInt() {
        int state = currentState;
        currentState = rng.draw() < transitionProb[currentState] ? currentState : 1 - currentState;
        return state;
    }
    
/**
* Returns a 2 element array with the parameters. The first element is copy of the transition
* probability array. The second element is the initial state as an Integer.
**/
    public Object[] getParameters() {
        return new Object[] { transitionProb.clone(), new Integer(initialState) };
    }
    
/**
* Sets the transition probabilities and (optionally) the initial state.
* @param params A 1 or 2 element array. The first element contains a two element
* <code>double</code> array with the probability that the state of the system will
* stay 0 given that it is 0 and the probability that the system will stay 1 given that it is 1.
* The second (optional) element is the initial state (0 or 1) of the system as a Number.
* @throws IllegalArgumentException If the array does not contain 1 or 2 elements,
* If the first element is not a 2 element <code>double</code> array with values
* between 0 and 1 inclusive, or if the 2nd (optional) element is not a Number
* with a value of 0 or 1.
*
**/
    public void setParameters(Object... params) {
        if (params.length != 1 && params.length != 2) {
            throw new IllegalArgumentException("A 1 or 2 element array is required.");
        }
        if (params.length == 1 && params[0] instanceof double[]) {
            setTransitionProb((double[]) params[0]);
        }
        else if (params.length == 2 && params[0] instanceof double[] && params[1] instanceof Number) {
            setTransitionProb((double[]) params[0]);
            setInitialState(((Number) params[1]).intValue());
        } else {
            throw new IllegalArgumentException("Either the first element is not a double[] or the second is not a Number");
        }
    }
    
/**
* Sets the transition probabilities.
* @param trans A two element array with the probability that the state of the system will
* stay 0 given that it is 0 and the probability that the system will stay 1 given that it is 1.
* @throws IllegalArgumentException If the array does not contain 2 elements or either element
* is not between 0 and 1 inclusive.
**/
    public void setTransitionProb (double[] trans) {
        if (trans.length == 2) {
            if (trans[0] >= 0.0 && trans[0] <= 1.0 && trans[1] >= 0.0 && trans[1] <= 1.0) {
            transitionProb = (double[]) trans.clone();
            }
            else {
                throw new IllegalArgumentException("Not probabilities: [" +
                    trans[0] + ", " + trans[1] + "]");
            }
        }
        else {
            throw new IllegalArgumentException("Need 2 probabilities: " + trans.length + " given");
        }
    }
    
    /**
     * Returns a 2 element array containing the transition probabilities.
     * @return probabilities of not changing state in 2-d array
     */    
    public double[] getTransitionProb() {
        return (double[]) transitionProb.clone();
    }
    
/**
* Sets the initial state (0 or 1) of the system
* @throws IllegalArgumentException If the value is not 0 or 1.
**/
    public void setInitialState(int state) {
        if (state == 0 || state == 1) {
            initialState = state;
            currentState = initialState;
        }
        else {
            throw new IllegalArgumentException("Initial state must be 0 or 1: " + state);
        }
    }
    
/**
* Returns the initial state of the system.
**/
    public int getInitialState() { return initialState; }
    
/**
* Returns a String containing the name of this RandomVariate, the transition
* probabilities and the initial state.
**/
    public String toString() {
        return "Two State Markov Chain [" + getTransitionProb()[0] + ", " +
            getTransitionProb()[1] + "] - {" + getInitialState() +"}";
    }
}
