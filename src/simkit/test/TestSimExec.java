package simkit.test;
import simkit.*;
import simkit.util.*;
/**
 *
 * @author  ahbuss
 */
public class TestSimExec {

    public static void main(String[] args) {
        SimExec simExec = new SimExec();
        simExec.setSingleStep(true);
        simExec.setStopTime(1000.0);
        System.out.println(simExec);
        
        javax.swing.SwingUtilities.invokeLater(simExec);
    }
    
}
