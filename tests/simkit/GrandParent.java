
package simkit;

import java.util.*;

public class GrandParent extends SimEntityBase implements TheInterface {

    protected List<Double> times = new ArrayList<Double>();

    public void doSomething(TheInterface i) {
        times.add(Schedule.getSimTime());
    }

    public String toString() {
        String ret = "GrandParent Processing Times: {";
        for (double time: times) {
            ret += time + " ";
        }
        ret += "}";
        return ret;
    }

    public int size() {return times.size();}
}
