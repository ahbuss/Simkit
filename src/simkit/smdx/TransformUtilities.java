/*
 * TransformUtilities.java
 *
 * Created on March 28, 2002, 11:08 PM
 */

package simkit.smdx;
import java.awt.*;
import java.awt.geom.*;
/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class TransformUtilities {

    public static RectangularShape createTransformedShape(RectangularShape shape, AffineTransform trans) {
        RectangularShape newShape = (RectangularShape) shape.clone();
        Rectangle2D bounds = newShape.getBounds2D();
        Shape transformedBounds = trans.createTransformedShape(bounds);
        Rectangle2D newBounds = transformedBounds.getBounds2D();
        newShape.setFrame(newBounds);
        return newShape;
    }
}