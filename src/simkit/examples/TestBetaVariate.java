package simkit.examples;

import java.text.DecimalFormat;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.SimpleStatsTally;

/**
 * <p>
 * A small example illustrating the use of RandomVariate and
 * RandomVariateFactory in generating random variates from a particular
 * distribution, in this case a Beta(2.3, 4.5).</p>
 * <p>
 * Note that in a simulation model, the SimpleStats instance will typically get
 * its values via the PropertyChangeListener pattern (i.e. it will be registered
 * as a PropertyChangeListener to a SimEntity that fire a PropertyChangeEvent
 * whenever the state variable of that name changes value).</p>
 *
 * 
 * @author ahbuss
 */
public class TestBetaVariate {

    public static void main(String[] args) {
//        The name of the distribution.  This could have been "BetaVariate",
//        the actual name of the class, or "simkit.random.BetaVariate", the
//        fully-qualified name of the class.  
        String rvName = "Beta";
//        Set up the values.  
        double alpha = 2.3;
        double beta = 4.5;

        Object[] rvParams = new Object[2];
        rvParams[0] = alpha;
        rvParams[1] = beta;

//        Obtain the instance from RandomVariateFactory.  This will return
//        null if a RandomVariate instance cannot be found in either the
//        simkit.random package or the serach path sepecified by the user.
        RandomVariate rv = RandomVariateFactory.getInstance(rvName, rvParams);
//        The toString() of a RandomVariate instance should give the name of 
//        the distribution and its parameter values.
        System.out.println("Using Object[]: " + rv);

        double mean = alpha / (alpha + beta);
        double var = alpha * beta / (Math.pow(alpha + beta, 2) * (alpha + beta + 1.0));

        DecimalFormat form = new DecimalFormat("0.0000");

        rv = RandomVariateFactory.getInstance(rvName, alpha, beta);
        System.out.println("Using varargs: " + rv);

//        The theoretical values of the Beta(alpha, beta)
        System.out.println("Mean = " + form.format(mean));
        System.out.println("Variance = " + form.format(var));
        System.out.println("Std Dev = " + form.format(Math.sqrt(var)));

//        This will collect observations and report summary statistics
        SimpleStatsTally stat = new SimpleStatsTally(rv.toString());

        int numberToGenerate = 100000;

//        Generate the values and have stat record summary statistics
        for (int i = 0; i < numberToGenerate; ++i) {
            double obs = rv.generate();
            stat.newObservation(obs);
        }

//        Output the values on one line.  The format of SimpleStats toString()
//        is: Count Min Max Mean Variance Std Dev        
        System.out.println("Statistics for Generated values:");
        System.out.println(stat);
    }
}
/* output:
Beta (2.3, 4.5)
Mean = 0.3382
Variance = 0.0287
Std Dev = 0.1694
Statistics for Generated values:
Beta (2.3, 4.5) (TALLY)
100000  0.0031  0.9595  0.3382  0.0286  0.1692
 */
