package simkit.test;

import simkit.stat.*;

public class TestSimpleStatsTally {

    public static void main(String[] args) {
        SimpleStatsTally sst = new SimpleStatsTally();
        SimpleStats ss = new SimpleStats();
        for (int i = 1; i < 1000001; i++) {
            sst.newObservation(i);
            ss.newObservation(i);
        }
        System.out.println(sst);
        System.out.println(ss);
    }
} 