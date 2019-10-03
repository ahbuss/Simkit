package simkit.test;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import simkit.random.DiscreteIntegerVariate;
import simkit.random.DiscreteRandomVariate;
import simkit.random.RandomVariateBase;
import simkit.random.RandomVariateFactory;
import simkit.stat.Histogram;

/**
 * @version $Id$
 * @author ahbuss
 */
public class TestDiscreteIntegerVariate {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        RandomVariateFactory.addSearchPackage("random");
        DiscreteRandomVariate rv
                = RandomVariateFactory.getDiscreteRandomVariateInstance(
                        "DiscreteInteger", new int[]{0, 5},
                        new double[]{1.0, 3.0});
        System.out.println(rv);

        Histogram histogram = new Histogram("Discrete Generated", 0, 5, 5);
        for (int i = 0; i < 100000; ++i) {
            histogram.newObservation(rv.generateInt());
        }
        System.out.println(histogram);

        rv = RandomVariateFactory.getDiscreteRandomVariateInstance(
                "DiscreteUniform", -50, 50);
        System.out.println(rv);
        histogram = new Histogram("Discrete Uniform", -50, 50, 100);
        for (int i = 0; i < 100000; ++i) {
            histogram.newObservation(rv.generateInt());
        }
        System.out.println(histogram);

        SortedMap<Integer, Double> frequencyMap = new TreeMap<>();
        frequencyMap.put(1, 10.0);
        frequencyMap.put(2, 20.0);
        frequencyMap.put(3, 30.0);
        frequencyMap.put(4, 40.0);

        rv = RandomVariateFactory.getDiscreteRandomVariateInstance("DiscreteInteger",
                frequencyMap.keySet().toArray(new Integer[0]),
                frequencyMap.values().toArray(new Double[0]));

        histogram = new Histogram("DiscreteInteger 2", 0, 4, 4);

        for (int i = 0; i < 1000000; ++i) {
            histogram.newObservation(rv.generate());
        }
        System.out.println(histogram);

        rv = RandomVariateFactory.getDiscreteRandomVariateInstance("DiscreteInteger", frequencyMap);
        histogram = new Histogram("DiscreteInteger 2", 1, 4, 3);

        for (int i = 0; i < 1000000; ++i) {
            histogram.newObservation(rv.generate());
        }
        System.out.println(histogram);

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(DiscreteIntegerVariate.class, RandomVariateBase.class);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                System.out.println(propertyDescriptor);
            }
        } catch (IntrospectionException ex) {
            Logger.getLogger(TestDiscreteIntegerVariate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
