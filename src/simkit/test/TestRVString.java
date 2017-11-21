/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simkit.test;

import simkit.random.RandomVariateFactory;

/**
 * @author Arnold Buss
 */
public class TestRVString {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         String[] tests = new String[] {
             " ConstantVariate (4.1) ", "Log Normal (10.0, 0.5)", "LogNormal (3.0, 2.0)",
             "Gamma(1.2, 3.4)", "Normal(0.0, 1.0)",
                  "BetaVariate(1.4,2.5", "Weibull (3, 1)",
                  "G a m m a (1.0 2.0)", "Constant"
         };
         
         for (String test: tests) {
             System.out.printf("%s: %b%n", test, RandomVariateFactory.isRandomVariateString(test));
         }
    }
    
}
