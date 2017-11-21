package simkit.test;

import java.beans.PropertyChangeEvent;
import simkit.SimEntity;
import simkit.SimEntityBase;
import simkit.random.RandomVector;
import simkit.random.RandomVectorFactory;
import simkit.stat.BivariateSimpleStatsTally;

/**
 *
 * @author ahbuss
 */
public class TestBivariateSimpleStatsTally {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SimEntity entity = new SimEntityBase() {
        };
        BivariateSimpleStatsTally bsst = new BivariateSimpleStatsTally("foo");
        entity.addPropertyChangeListener("foo", bsst);

        RandomVector rv = RandomVectorFactory.getInstance("BivariateNormal",
                1.0, 2.5, 0.75, 2.2, 0.6);
        System.out.println(rv);
        int number = 1000000;
        for (int i = 0; i < number; ++i) {
            entity.firePropertyChange(
                    new PropertyChangeEvent(entity, "foo", null, rv.generate()));
        }
        System.out.println(bsst);
    }

}
