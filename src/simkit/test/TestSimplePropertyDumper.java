package simkit.test;

import simkit.SimEntityBase;
import simkit.util.SimplePropertyDumper;

/**
 *
 * @author ahbuss
 */
public class TestSimplePropertyDumper {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SimEntityBase simEntityBase = new SimEntityBase(){};
        simEntityBase.setName("Testing");
        SimplePropertyDumper simplePropertyDumper =
                new SimplePropertyDumper();
        simEntityBase.addPropertyChangeListener(simplePropertyDumper);
        SimplePropertyDumper simplePropertyDumper2 =
                new SimplePropertyDumper(true);
        simEntityBase.addPropertyChangeListener("baz", simplePropertyDumper2);
        simEntityBase.addPropertyChangeListener("bar", simplePropertyDumper2);
        simEntityBase.addPropertyChangeListener(simplePropertyDumper2);
        simEntityBase.firePropertyChange("foo", 17.0);
        simEntityBase.firePropertyChange("aproperty", 5, 6);
        simEntityBase.fireIndexedPropertyChange(3, "bar", true);
        simEntityBase.fireIndexedPropertyChange(5, "baz", "Hi", "Mom");
        simEntityBase.firePropertyChange("baz", 1.0, 1.0);
        
//        System.out.println(Arrays.toString(simEntityBase.getPropertyChangeListeners()));
                
    }

}
