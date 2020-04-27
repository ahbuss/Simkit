package simkit.test;

import simkit.Schedule;
import simkit.SimEntity;
import simkit.examples.ArrivalProcess;
import simkit.random.RandomVariateFactory;
import simkit.util.UnitTestEventList;

/**
 *
 * @author ahbuss
 */
public class TestUnitTestEventList {

    public static void main(String[] args) {
        int id = Schedule.addNewEventList(UnitTestEventList.class);
        UnitTestEventList testEventList = (UnitTestEventList) Schedule.getEventList(id);
        Schedule.setDefaultEventList(testEventList);

        ArrivalProcess arrivalProcess = new ArrivalProcess(
                RandomVariateFactory.getInstance("Constant", 1.1));
//        arrivalProcess.setEventListID(id);

        System.out.println(testEventList.getRerun());

        Schedule.reset();

        testEventList.dump();
    }
}
