package simkit;
/////////////////////////// Copyright Notice //////////////////////////
//                                                                   //
// This simkit package or sub-package and this file is Copyright (c) //
// 1997, 1998, 1999 by Kirk A. Stork and Arnold H. Buss under the    //
// GNU public license.                                               //
//                                                                   //
// Please forward any changes, comments or suggestions to:           //
//   abuss@nps.navy.mil                                              //
//                                                                   //
///////////////////////////////////////////////////////////////////////

/**
 *  This class provides two ways to "Stop" a simulation.  The first, the Stop
 *  event stops the entire simulation.  The second only cancels the events scheduled
 * by a particular SimEnity.
 * @version $Id$
 * @author Arnold Buss
**/
public class Stop extends SimEntityBase {

/**
 *  Stop the simulation.
**/
    public void doStop() {
       Schedule.stopSimulation();
    }

/**
 *  Stop the simulation and reset.  Typically, all SimEntities will also be
 *  reset by Schedule.reset() as well.
**/
    public void doStopAndReset() {
        Schedule.stopSimulation();
        Schedule.reset();
    }

/**
 *  "Stop" the given SimEntity by interrupting all of its pending methods.
 * @param stopping The SimEntity that will have its events stopped.
**/
    public void doStopSimEntity(SimEntity stopping) {
        stopping.interruptAll();
    }

/**
 *  "Stop" the given SimEntity by interrupting all of its pending methods. Issue
 *  a reset() as well, which usually will put the SimEntity back in its (nearly)
 *  pristine state.
 *  @param stopping The SimEntity that will have its events stopped.
**/
    public void doStopAndResetSimEntity(SimEntity stopping) {
        stopping.interruptAll();
        stopping.reset();
    }

/**
* Schedule the simulation to stop at the given time.
**/
    public void stopAtTime(double time) {
        new Stop().waitDelay("Stop", time);
    }

/**
* Schedule the given SimEntity to be stopped at the given time.
**/
    public static void stopSimEntityAtTime(SimEntity entity, double time) {
        new Stop().waitDelay("StopSimEntity", time, entity);
    }

/**
* Schedule the given SimEntity to be stopped and reset at the given time.
**/
    public static void stopAndResetSimEntityAtTime(SimEntity entity, double time) {
        new Stop().waitDelay("StopAndResetSimEntity", time, entity);
    }


}
