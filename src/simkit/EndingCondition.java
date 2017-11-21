package simkit;

/////////////////////////// Copyright Notice //////////////////////////
//                                                                   //
// This simkit package or sub-package and this file is Copyright (c) //
// 1997, 1998, 1999 by Kirk A. Stork and Arnold H. Buss.             //
//                                                                   //
// Please forward any changes, comments or suggestions to:           //
//   abuss@nps.navy.mil                                              //
//                                                                   //
///////////////////////////////////////////////////////////////////////
/**
 * An enumeration to specify the ending condition. At the moment, only two are
 * specified: STOP_ON_TIME stops the simulation at a prescribed time and
 * STOP_ON_EVENT stops after an event has reached a prespecified count (See the
 * implementation of simkit.Schedule). Adding additional types requires
 * modifying this class.
 *
 * @author Arnold Buss (revised 28 March 1999)
 * @version $Id$
 */
public class EndingCondition {

    /**
     * The name of the ending condition.
     */
    private final String name;

    /**
     * Create a new ending condition with the given name.
     *
     * @param name The name of the ending condition
     */
    protected EndingCondition(String name) {
        this.name = name;
    }

    /**
     * Stop at a specified time.
     */
    public static final EndingCondition STOP_ON_TIME = new EndingCondition("STOP_ON_TIME");

    /**
     * Stop due to a specified event.
     */
    public static final EndingCondition STOP_ON_EVENT = new EndingCondition("STOP_ON_EVENT");

    /**
     * @return the name of the condition.
     */
    @Override
    public String toString() {
        return name;
    }
}
