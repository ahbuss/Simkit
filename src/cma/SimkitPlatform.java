package cma;
import java.util.logging.Logger;

/**
 * Concrete implementation of a bare-bones simulation entity that draws its
 * behaviors from delegate modules added at run time.  If the design holds,
 * there should be little or no need to subclass this object.
 *
 * @author Kirk Stork, The MOVES Insititute, NPS
 * @version $Id $
 */
public class SimkitPlatform implements Platform {

    public static final String _VERSION_ = "$Id$";
    public static final Logger LOG = Logger.getLogger("cma");


}
