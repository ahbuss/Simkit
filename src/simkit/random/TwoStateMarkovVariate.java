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
 */
public class TwoStateMarkovVariate extends RandomVariateBase implements DiscreteRandomVariate {
    
    private double[] transitionProb;
    private int initialState;
    protected int currentState;
    
    public TwoStateMarkovVariate() {
    }
    
    /** Generate a random variate having this class's distribution.
     * @return The generated random variate
     */
    public double generate() {
        return (double) generateInt();
    }
    
    /**
     * @return generated value
     */    
    public int generateInt() {
        int state = currentState;
        currentState = rng.draw() < transitionProb[currentState] ? currentState : 1 - currentState;
        return state;
    }
    
    /** Returns the array of parameters as an Object[].
     * @return the array of parameters as an Object[].
     */
    public Object[] getParameters() {
        return new Object[] { transitionProb.clone(), new Integer(initialState) };
    }
    
    /**  Sets the random variate's parameters.
     *  Alternatively, the parameters could be set in the constructor or
     *  in additional methods provided by the programmer.
     * @param params the array of parameters, wrapped in objects.
     */
    public void setParameters(Object[] params) {
        if (params.length == 1 && params[0] instanceof double[]) {
            setTransitionProb((double[]) params[0]);
        }
        else if (params.length == 2 && params[0] instanceof double[] && params[1] instanceof Number) {
            setTransitionProb((double[]) params[0]);
            setInitialState(((Number) params[1]).intValue());
        }
    }
    
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
            throw new IllegalArgumentException("Need 2 probabilities: " + trans.length);
        }
    }
    
    /**
     * @return probabilities of not changing state in 2-d array
     */    
    public double[] getTransitionProb() {
        return (double[]) transitionProb.clone();
    }
    
    public void setInitialState(int state) {
        if (state == 0 || state == 1) {
            initialState = state;
            currentState = initialState;
        }
        else {
            throw new IllegalArgumentException("Initial state must be 0 or 1: " + state);
        }
    }
    
    public int getInitialState() { return initialState; }
    
    public String toString() {
        return "Two State Markov Chain [" + getTransitionProb()[0] + ", " +
            getTransitionProb()[1] + "] - {" + getInitialState() +"}";
    }
}
