/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simkit.test;

import java.awt.geom.Point2D;
import simkit.Schedule;
import simkit.SimEntityBase;
import simkit.smd.BasicLinearMover;
import simkit.smd.CookieCutterSensor;
import simkit.smd.Mover;
import simkit.smd.Sensor;

/**
 *
 * @author ahbuss
 */
class SuperClassTest extends SimEntityBase {

    public void doThis(Sensor sensor) {
        System.out.println("This with Sensor: " + sensor);
    }
}

public class TestOverload extends SuperClassTest {

    private Sensor mySensor;

    public void doRun() {
        waitDelay("This", 10.0, mySensor);
    }

    public void doThis(CookieCutterSensor sensor) {
        System.out.println("This with CookieCutterSensor: " + sensor);
    }

    public Sensor getMySensor() {
        return mySensor;
    }

    public void setMySensor(Sensor mySensor) {
        this.mySensor = mySensor;
    }

    public static void main(String[] args) {
        TestOverload testOverload = new TestOverload();
        Mover mover = new BasicLinearMover(new Point2D.Double(10.0, 20.0), 0.0);
        testOverload.setMySensor(new CookieCutterSensor(mover, 20.0));

        Schedule.setVerbose(true);

        Schedule.reset();
        Schedule.startSimulation();

    }

}
