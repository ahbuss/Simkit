package simkit.smdx.animate;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.Icon;
import simkit.smdx.Mover;

/**
 * @version $Id$
 * @author  Arnold Buss
 */
public class TargetIcon extends MoverIcon implements PropertyChangeListener {
    
    protected Icon[] icons;
    
    /** Creates a new instance of TargetIcon */
    public TargetIcon(Mover mover, Icon[] icons, Point2D origin) {
        super(mover, icons[0], origin);
        this.icons = (Icon[]) icons.clone();
        if (icons.length < 3) {
            throw new IllegalArgumentException("Need at least 3 icons: " + icons.length);
        }
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        switch (e.getPropertyName()) {
            case "glimpse":
                if (e.getNewValue().equals(Boolean.FALSE)) {
                    setIcon(icons[1]); 
                }
                else {
                    setIcon(icons[2]);
                }
                break;
            case "exitRange":
                setIcon(icons[0]);
                break;
            case "reset":
                setIcon(icons[0]);
                break;
        }
    }
    
}
