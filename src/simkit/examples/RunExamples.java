package simkit.examples;

import simkit.random.RandomVariateFactory;
import simkit.Schedule;

public class RunExamples {

    public static void main(String[] args) {
        double stopTime = 100.0;
        boolean singleStep = true;

        if (args.length == 0) {
            usage("examples.RunExamples <Class name> <parameters>");
            return;
        }
        if (args[0].equals("ArrivalProcess")) {
            if (args.length < 3) {
                usage("examples.ArrivalProcess <mean interarrival time > <stop time> [single-step]");
                return;
            }
            if (args.length == 4) {
                singleStep = new Boolean(args[3]).booleanValue();   
            }
            ArrivalProcess arrivals = new ArrivalProcess(
                RandomVariateFactory.getInstance(
                    "simkit.random.ExponentialVariate", new Object[] { new Double(args[1]) },
                    simkit.random.CongruentialSeeds.SEED[0]));
                stopTime = Double.parseDouble(args[2]);
                System.out.println(arrivals.paramString());
                System.out.println();

                System.out.println("Simulation ends at time " + stopTime);
                Schedule.setSingleStep(singleStep);
                Schedule.stopAtTime(stopTime);
                Schedule.reset();
                Schedule.startSimulation();

                System.out.println("At time " + Schedule.getSimTime() + " there have been " +
                    arrivals.getNumberArrivals() + " arrivals");
        }
        else if (args[0].equals("MultipleServerQueue") ) {
            if (args.length == 2) {
                singleStep = new Boolean(args[1]).booleanValue();
            }
            MultipleServerQueue msq = new MultipleServerQueue(2,
                RandomVariateFactory.getInstance("simkit.random.ExponentialVariate", new Object[] { new Double(1.7) },
                    simkit.random.CongruentialSeeds.SEED[0]),
                RandomVariateFactory.getInstance("simkit.random.ExponentialVariate", new Object[] { new Double(1.5) },
                    simkit.random.CongruentialSeeds.SEED[1]) );
            System.out.println(msq.paramString());

            simkit.util.PropertyChangeFrame pcf = new simkit.util.PropertyChangeFrame();
            msq.addPropertyChangeListener(pcf);
            pcf.setVisible(true);

            Schedule.setSingleStep(singleStep);
            Schedule.stopAtTime(stopTime);
            Schedule.reset();
            Schedule.startSimulation();

        }


    }

    private static void usage(String message) {
        System.err.println("Usage: java " + message);
        System.exit(1);
    }
}