package simkit.assembly;
public class ServerWithRenegesAssembly1 {
    public static void main(java.lang.String[] args) {
//        Create SimEntities
        simkit.examples.ArrivalProcess arrival = new simkit.examples.ArrivalProcess(
            simkit.random.RandomVariateFactory.getInstance(
                "Exponential",
                new java.lang.Object[] { 
                    new java.lang.Double(1.5) 
                }, 
                simkit.random.CongruentialSeeds.SEED[0]
            )
        );
        simkit.examples.ServerWithReneges server = new simkit.examples.ServerWithReneges(
            2,
            simkit.random.RandomVariateFactory.getInstance(
                "Gamma",
                new java.lang.Object[] { 
                    new java.lang.Double(2.5), 
                    new java.lang.Double(1.2) 
                },
                simkit.random.CongruentialSeeds.SEED[1]
            ),
            simkit.random.RandomVariateFactory.getInstance(
                "Uniform",
                new java.lang.Object[] { new java.lang.Double(4.0), new java.lang.Double(6.0) },
                simkit.random.CongruentialSeeds.SEED[2]
            )
        );
        simkit.examples.CollectionSizeListener csl = new simkit.examples.CollectionSizeListener();                
        simkit.examples.IntegerArrivalListener ial = new simkit.examples.IntegerArrivalListener();
        simkit.examples.TimeListener time = new simkit.examples.TimeListener();
//        Create PropertyChangeListeners
        java.beans.PropertyChangeListener numInQueueStat = new simkit.stat.SimpleStatsTimeVarying("queue.size");
        java.beans.PropertyChangeListener numAvailServerStat = new simkit.stat.SimpleStatsTimeVarying("numberAvailableServers");
        java.beans.PropertyChangeListener timeInSystemStat = new simkit.stat.SimpleStatsTally("timeInSystem");
        java.beans.PropertyChangeListener delayInQueueStat = new simkit.stat.SimpleStatsTally("delayInQueue");
        java.beans.PropertyChangeListener delayInQueueRenegeStat = new simkit.stat.SimpleStatsTally("delayInQueueRenege");
        java.beans.PropertyChangeListener delayInQueueServedStat = new simkit.stat.SimpleStatsTally("delayInQueueServed");
//        Connect SimEventListeners
        arrival.addSimEventListener(ial);
        ial.addSimEventListener(server);
        ial.addSimEventListener(time);
        server.addSimEventListener(time);
//        Connect PropertyChangeListeners
        server.addPropertyChangeListener(csl);
        server.addPropertyChangeListener(time);
        csl.addPropertyChangeListener("queue.size", numInQueueStat);
        server.addPropertyChangeListener("numberAvailableServers", numAvailServerStat);
        time.addPropertyChangeListener("timeInSystem", timeInSystemStat);
        time.addPropertyChangeListener("delayInQueue", delayInQueueStat);
        time.addPropertyChangeListener("delayInQueueRenege", delayInQueueRenegeStat);
        time.addPropertyChangeListener("delayInQueueServed", delayInQueueServedStat);
//        Print selected SimEntities
        System.out.println(arrival);
        System.out.println(server);
    }
}
