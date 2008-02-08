/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simkit.test;

import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.SimpleStatsTally;

/**
 * @veresion $Id$
 * @author ahbuss
 */
public class TestInverseGussianVariate {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RandomVariate rv = RandomVariateFactory.getInstance(
                "InverseGaussian", 76.0, 103.0);
        System.out.println(rv);
        SimpleStatsTally tally = new SimpleStatsTally("Inverse Guassian");
        for (int i = 0; i < 100000; ++i) {
            tally.newObservation(rv.generate());
        }
        System.out.println(tally);
    }

}
