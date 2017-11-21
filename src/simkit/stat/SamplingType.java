package simkit.stat;

/**
 * A Java-style "enumeration" class to represent the type of sampling being done
 * by a given class. Current options are TALLY and TIME_VARYING. LINEAR is not
 * yet implemented.
 *
 * @author Arnold Buss
 * @version $Id$
*
 */
public enum SamplingType {

    /**
     * For statistics that are collected one at a time, independent of time.
     */
    TALLY,
    /**
     * For statistics that represent values that vary over time according yo
     * admissible DES state trajectory rules (i.e. Piecewise constant)
     */
    TIME_VARYING,
    /**
     * For state trajectories that are piecewise linear. Note that these must be
     * "implied" state trajectories. None have yet been implemented.
     */
    LINEAR

}
