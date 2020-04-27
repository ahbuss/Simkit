package simkit.random;

import java.util.ArrayList;

/**
 * Generates the composition of a list of RandomVariates. These are specified
 * by the parameters, a list of names and of associated parameters for each
 * RandomVariate.
 * 
 * 
 * @author ahbuss
 */
public class CompositionVariate extends RandomVariateBase {

    private RandomVariate[] theRandomVariates;
        
    @Override
    public double generate() {
        double value = 0.0;
        
        for (RandomVariate rv : theRandomVariates) {
            value += rv.generate();
        }
        
        return value;
    }

    @Override
    public void setParameters(Object... params) {
        if (params.length != 2) {
            throw new IllegalArgumentException(
                    "CompositionVariate must have 2 argumets : " +
                    params.length);
        }
        if (params[0].getClass() != String[].class || params[1].getClass() != Object[][].class) {
            throw new IllegalArgumentException(
                    "CompositionVariate arguments must be String[] and Object[][]: " +
                    params[0].getClass().getName() + ", " + params[1].getClass().getName());
        }
        String[] names = (String[]) params[0];
        Object[][] param = (Object[][]) params[1];
        
        if (names.length != param.length) {
            throw new IllegalArgumentException(
                    "CompositionVariate must have same number of names as arguments: " +
                    names.length + " vs " + param.length) ;
        }
        
        theRandomVariates = new RandomVariate[param.length];
        for (int i = 0; i < theRandomVariates.length; ++i) {
            theRandomVariates[i] = RandomVariateFactory.getInstance(names[i], param[i]);
        }
    }

    @Override
    public Object[] getParameters() {
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<Object[]> param = new ArrayList<Object[]>();
        for (RandomVariate rv : theRandomVariates) {
            String name = rv.getClass().getName();
            names.add(name.substring(name.lastIndexOf(".")));
            param.add(rv.getParameters());
        }
        return new Object[] { 
            names.toArray(new String[0]),
            param.toArray(new Object[0])
        };
    }

    /**
     * @return the theRandomVariates
     */
    public RandomVariate[] getTheRandomVariates() {
        return theRandomVariates;
    }
    
    @Override
    public String toString() {
        String toString = "Composition " ;
        for (RandomVariate rv : theRandomVariates) {
            toString += System.getProperty("line.separator");
            toString += "\t";
            toString += rv.toString();
        }
        return toString;
    }
    
}
