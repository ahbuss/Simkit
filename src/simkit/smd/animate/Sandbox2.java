package simkit.smd.animate;

import simkit.animate.Sandbox;
import java.awt.Component;

/**
 * @version $Id: Sandbox2.java 81 2009-11-16 22:28:39Z ahbuss $
 * @author ahbuss
 */
public class Sandbox2 extends Sandbox {

    @Override
    public Component add(Component c) {
        if (c instanceof MoverIcon) {
            ((MoverIcon) c).setOrigin(getOrigin());
        }
        return super.add(c);
    }
}
