package simkit.examples;

import java.util.Calendar;
import java.util.Date;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import simkit.random.RandomNumberFactory;
import simkit.random.RngStream;

/**
 * Just a place to experiment with RngStream.  Eventually things
 * here will move to Unit Test coverage.
 * 
 * @version $Id$
 * @author Kirk Stork (The MOVES Institute)
 */
public class RngStreamExamples {

    static final RngStreamExamples SE = new RngStreamExamples();
    Map<String, RngStream> streams = new HashMap();
    int i = 0;
    int j = 100;

    public static void main(String args[]) {
    }

    public RngStreamExamples() {
        
        streams.put("S000", new RngStream(0,0));
        streams.put("S100", new RngStream(1,0));
        streams.put("S200", new RngStream(2,0));
        streams.put("S010", new RngStream(0,1));
        streams.put("S110", new RngStream(1,1));
        streams.put("S210", new RngStream(2,1));
        
        // See roughly how long it takes to generate stream[1000:1000]
        // 1000 times.
        
        Calendar c = Calendar.getInstance();
        long t0 = c.getTimeInMillis();


        for (int p = 0; p < 1000; p++ ) {
            new RngStream(1000,10000);
        }
        
        System.out.println(Calendar.getInstance().getTimeInMillis() - t0);
    }

    private void writeStates() {
        for (String k : streams.keySet()) {
            System.out.println("Stream " + k);
            streams.get(k).writeStateFull();
        }
    }

    private void draw4() {
        for (String k : streams.keySet()) {
            System.out.println("Stream " + k);
            System.out.print(streams.get(k).randInt(i, j) + ",\t");
            System.out.print(streams.get(k).randInt(i, j) + ",\t");
            System.out.print(streams.get(k).randInt(i, j) + ",\t");
            System.out.println(streams.get(k).randInt(i, j));
        }
    }

    private void draw4(String k) {
        System.out.println("Stream " + k);
        System.out.print(streams.get(k).randInt(i, j) + ",\t");
        System.out.print(streams.get(k).randInt(i, j) + ",\t");
        System.out.print(streams.get(k).randInt(i, j) + ",\t");
        System.out.println(streams.get(k).randInt(i, j));
    }
}
