package simkit.test;

import simkit.Schedule;
import simkit.SimEntityBase;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.LinearSimpleStatsTimeVarying;
import simkit.util.SimplePropertyDumper;

/**
 * @version $Id$
 * @author ahbuss
 */
public class TestLinearSimpleStatsTimeVarying extends SimEntityBase {
    
    private RandomVariate interarrivalTime;
    
    private RandomVariate inventoryLevel;
    
    private double level;
    
    /**
     * Creates a new instance of TestLinearSimpleStatsTimeVarying 
     * @param interarrivalTime Given interarrivalTime
     * @param inventoryLevel Given inventory level
     */
    public TestLinearSimpleStatsTimeVarying(RandomVariate interarrivalTime,
            RandomVariate inventoryLevel) {
        setInterarrivalTime(interarrivalTime);
        setInventoryLevel(inventoryLevel);
    }
    
    public void reset() {
        super.reset();
        level = 0.0;
    }
    
    public void doRun() {
        firePropertyChange("level", getLevel());
        
        waitDelay("GetStuff", interarrivalTime.generate());
    }
    
    public void doGetStuff() {
        level = inventoryLevel.generate();
        firePropertyChange("level", getLevel());
        waitDelay("GetStuff", interarrivalTime.generate());
    }
    
    public double getLevel() {
        return level;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double[] times = new double[] { 1.0, 0.0, 2.0, 0.0, 1.0, 0.0, 2.0 };
        
        double[] level = new double[] { 0, 5, 2, 7, 1, 4, 0 };
        
        RandomVariate iat = RandomVariateFactory.getInstance(
                "Trace", new Object[] { times } );
        RandomVariate inv = RandomVariateFactory.getInstance(
                "Trace", new Object[] { level } );
        
        TestLinearSimpleStatsTimeVarying test = new TestLinearSimpleStatsTimeVarying(iat, inv);
        
        System.out.println(test);
        
        SimplePropertyDumper simplePropertyDumper = new SimplePropertyDumper();
        test.addPropertyChangeListener(simplePropertyDumper);
        
        LinearSimpleStatsTimeVarying linearSimpleStatsTimeVarying =
                new LinearSimpleStatsTimeVarying("level");
        test.addPropertyChangeListener(linearSimpleStatsTimeVarying);
        
        Schedule.setVerbose(true);
        
        Schedule.reset();
        Schedule.startSimulation();
        
        System.out.println(linearSimpleStatsTimeVarying);
        
    }

    public RandomVariate getInterarrivalTime() {
        return interarrivalTime;
    }

    public void setInterarrivalTime(RandomVariate interarrivalTime) {
        this.interarrivalTime = interarrivalTime;
    }

    public RandomVariate getInventoryLevel() {
        return inventoryLevel;
    }

    public void setInventoryLevel(RandomVariate inventoryLevel) {
        this.inventoryLevel = inventoryLevel;
    }
    
} /* output
simkit.test.TestLinearSimpleStatsTimeVarying.1
        interarrivalTime = Trace: [1.000,0.000,2.000,0.000,1.000,0.000,2.000] (7 values)
        inventoryLevel = Trace: [0.000,5.000,2.000,7.000,1.000,4.000,0.000] (7 values)
** Event List 0 -- Starting Simulation **
0.000	Run	
 ** End of Event List -- Starting Simulation **

level: null => 0.0
Time: 0.0000	CurrentEvent: Run [1]
** Event List 0 --  **
1.000	GetStuff	
 ** End of Event List --  **

level: null => 0.0
Time: 1.0000	CurrentEvent: GetStuff [1]
** Event List 0 --  **
1.000	GetStuff	
 ** End of Event List --  **

level: null => 5.0
Time: 1.0000	CurrentEvent: GetStuff [2]
** Event List 0 --  **
3.000	GetStuff	
 ** End of Event List --  **

level: null => 2.0
Time: 3.0000	CurrentEvent: GetStuff [3]
** Event List 0 --  **
3.000	GetStuff	
 ** End of Event List --  **

level: null => 7.0
Time: 3.0000	CurrentEvent: GetStuff [4]
** Event List 0 --  **
4.000	GetStuff	
 ** End of Event List --  **

level: null => 1.0
Time: 4.0000	CurrentEvent: GetStuff [5]
** Event List 0 --  **
4.000	GetStuff	
 ** End of Event List --  **

level: null => 4.0
Time: 4.0000	CurrentEvent: GetStuff [6]
** Event List 0 --  **
6.000	GetStuff	
 ** End of Event List --  **

level: null => 0.0
Time: 6.0000	CurrentEvent: GetStuff [7]
** Event List 0 --  **
            << empty >>
 ** End of Event List --  **

level (LINEAR)
8 0.000 7.000 2.500 3.028 1.740
   */