package examples;

import simkit.*;
import simkit.random.*;
import simkit.stat.*;
import simkit.util.*;
import java.text.*;

public class ServerWithRenegesAssembly1 {

    public static void main(String[] args) {

        examples.ArrivalProcess arrival = new examples.ArrivalProcess(
                simkit.random.RandomVariateFactory.getInstance(
                        "Exponential",
                        new Object[]{
                            new java.lang.Double(1.5)
                        },
                        simkit.random.CongruentialSeeds.SEED[0]
                )

        );

        examples.ServerWithReneges server = new examples.ServerWithReneges(
                2,
                simkit.random.RandomVariateFactory.getInstance(
                        "Gamma",
                        new Object[]{
                            new java.lang.Double(2.5),
                            new java.lang.Double(1.2)
                        },
                        simkit.random.CongruentialSeeds.SEED[1]
                ),
                simkit.random.RandomVariateFactory.getInstance(
                        "Uniform",
                        new Object[]{
                            new java.lang.Double(4.0),
                            new java.lang.Double(6.0)
                        },
                        simkit.random.CongruentialSeeds.SEED[2]
                )

        );

        examples.CollectionSizeListener csl = new examples.CollectionSizeListener();

        examples.IntegerArrivalListener ial = new examples.IntegerArrivalListener();

        examples.TimeListener time = new examples.TimeListener();


        java.beans.PropertyChangeListener numInQueueStat = 
            new simkit.random.SimpleStateTimeVarying("numberInQueue");

        java.beans.PropertyChangeListener numAvailableServerStat = 
            new simkit.random.SimpleStateTimeVarying("numberAvailableServers");

        java.beans.PropertyChangeListener timeInSystemStat = 
            new simkit.random.SimpleStateTally("timeInSystem");

        java.beans.PropertyChangeListener delayInQueueStat = 
            new simkit.random.SimpleStateTally("delayInQueue");

        java.beans.PropertyChangeListener delayInQueueRenegeStat = 
            new simkit.random.SimpleStateTally("delayInQueueRenege");

        java.beans.PropertyChangeListener delayInQueueServedStat = 
            new simkit.random.SimpleStateTally("delayInQueueServed");

        arrival.addSimEventListener(ial);
        ial.addSimEventListener(server);
        ial.addSimEventListener(time);
        server.addSimEventListener(time);

        server.addPropertyChangeListener(csl);
        server.addPropertyChangeListener(time);
        csl.addPropertyChangeListener("queue.size", numInQueueStat);
        server.addPropertyChangeListener("numberAvailableServers", numAvailableServerStat);
        time.addPropertyChangeListener("timeInSystem", timeInSystemStat);
        time.addPropertyChangeListener("delayInQueue", delayInQueueStat);
        time.addPropertyChangeListener("delayInQueueRenege", delayInQueueRenegeStat);
        time.addPropertyChangeListener("delayInQueueServed", delayInQueueServedStat);

        System.out.println(server);
        System.out.println(arrival);

}

