/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simkit.util;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.logging.Logger;
import simkit.SimEvent;
import simkit.SimEventListener;

/**
 *
 * @author Kirk Stork, The MOVES Insititute, NPS
 * @version $Id: $
 */
public class SimpleEventLogger implements SimEventListener {

    public static final String _VERSION_ = "$Id: $";
    public static Logger log = Logger.getLogger("simkit.util");

    static final Format form = new DecimalFormat("0.0000");

//    @Override
    public void processSimEvent(SimEvent event) {
        System.out.println(form.format(event.getScheduledTime()) +
                ":\t" + event.getEventName());
    }


}
