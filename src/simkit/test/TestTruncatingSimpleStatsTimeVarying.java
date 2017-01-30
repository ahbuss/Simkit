package simkit.test;

import simkit.Schedule;
import simkit.SimEntityBase;
import simkit.examples.ArrivalProcess;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.SimpleStatsTimeVarying;
import simkit.stat.TruncatingSimpleStatsTimeVarying;
import simkit.util.SimplePropertyDumper;

/**
 *
 * @author ahbuss
 */
public class TestTruncatingSimpleStatsTimeVarying extends SimEntityBase {

    private RandomVariate generator;

    private int initialValue;

    protected int value;

    public TestTruncatingSimpleStatsTimeVarying() {

    }

    public TestTruncatingSimpleStatsTimeVarying(RandomVariate generator,
            int initialValue) {
        this.setGenerator(generator);
        this.setInitialValue(initialValue);
    }

    public void reset() {
        super.reset();
        this.value = getInitialValue();
    }

    public void doRun() {
        firePropertyChange("value", getValue());
    }

    public void doArrival() {
        int oldValue = getValue();
        this.value = (int) getGenerator().generate();
        firePropertyChange("value",  getValue());
    }

    /**
     * @return the generator
     */
    public RandomVariate getGenerator() {
        return generator;
    }

    /**
     * @param generator the generator to set
     */
    public void setGenerator(RandomVariate generator) {
        this.generator = generator;
    }

    /**
     * @return the initialValue
     */
    public int getInitialValue() {
        return initialValue;
    }

    /**
     * @param initialValue the initialValue to set
     */
    public void setInitialValue(int initialValue) {
        this.initialValue = initialValue;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RandomVariate interarrivalTime
                = RandomVariateFactory.getInstance("Exponential", 1.2);
        ArrivalProcess arrivalProcess = new ArrivalProcess();
        arrivalProcess.setInterarrivalTimeGenerator(interarrivalTime);

        int initialValue = 2;
        RandomVariate rv
                = RandomVariateFactory.getDiscreteRandomVariateInstance(
                        "DiscreteUniform",0, 2);
        TestTruncatingSimpleStatsTimeVarying tsstv = 
                new TestTruncatingSimpleStatsTimeVarying(rv, initialValue);
        
        System.out.println(arrivalProcess);
        System.out.println(tsstv);
        
        SimplePropertyDumper simplePropetyDumper = new SimplePropertyDumper();
        tsstv.addPropertyChangeListener(simplePropetyDumper);
        
        arrivalProcess.addSimEventListener(tsstv);
        
        TruncatingSimpleStatsTimeVarying stats = 
                new TruncatingSimpleStatsTimeVarying("value", 5.0);
        tsstv.addPropertyChangeListener(stats);
        SimpleStatsTimeVarying sstv = new SimpleStatsTimeVarying("value");
        tsstv.addPropertyChangeListener(sstv);
        Schedule.stopOnEvent(10, "Arrival");
        
        Schedule.setVerbose(true);
        
        Schedule.reset();
        Schedule.startSimulation();
        
        System.out.printf("Simulation ended at %,.3f%n", Schedule.getSimTime());
        System.out.println(sstv);
        System.out.println(stats);
    }

}
