package simkit;

/**
 * This class provides two ways to "Stop" a simulation. The first, the Stop
 * event stops the entire simulation. The second only cancels the events
 * scheduled by a particular SimEnity.
 *
 *
 * @author Arnold Buss
 */
public class Stop extends SimEntityBase {

    public Stop() {
        super();
    }

    public Stop(BasicEventList eventList) {
        super();
        this.eventList = eventList;
    }

    /**
     * Stop the simulation.
     */
    public void doStop() {
        getEventList().stopSimulation();
    }

    /**
     * Stop the simulation and reset. Typically, all SimEntities will also be
     * reset by Schedule.reset() as well.
     */
    public void doStopAndReset() {
        getEventList().stopSimulation();
        getEventList().reset();
    }

    /**
     * "Stop" the given SimEntity by interrupting all of its pending methods.
     *
     * @param stopping The SimEntity that will have its events stopped.
     */
    public void doStopSimEntity(SimEntity stopping) {
        stopping.interruptAll();
    }

    /**
     * "Stop" the given SimEntity by interrupting all of its pending methods.
     * Issue a reset() as well, which usually will put the SimEntity back in its
     * (nearly) pristine state.
     *
     * @param stopping The SimEntity that will have its events stopped.
     */
    public void doStopAndResetSimEntity(SimEntity stopping) {
        stopping.interruptAll();
        stopping.reset();
    }

    /**
     * Schedule the simulation to stop at the given time.
     *
     * @param time given time
     */
    @Override
    public void stopAtTime(double time) {
        new Stop().waitDelay("Stop", time, Priority.LOWEST);
    }

    /**
     * Schedule the given SimEntity to be stopped at the given time.
     *
     * @param entity given SimEntity
     * @param time given time
     */
    public static void stopSimEntityAtTime(SimEntity entity, double time) {
        new Stop().waitDelay("StopSimEntity", time, entity, Priority.LOWEST);
    }

    /**
     * Schedule the given SimEntity to be stopped and reset at the given time.
     *
     * @param entity given SimEntity
     * @param time given time
     */
    public static void stopAndResetSimEntityAtTime(SimEntity entity, double time) {
        new Stop().waitDelay("StopAndResetSimEntity", time, entity, Priority.LOWEST);
    }

}
