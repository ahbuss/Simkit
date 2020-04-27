/*
 * TransformUtilities.java
 *
 * Created on March 28, 2002, 11:08 PM
 */
package simkit.smdx;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

/**
 * Contains a method to transform the coordinates of a rectangle.
 *
 * @author Arnold Buss
 * 
 */
public class TransformUtilities {

    /**
     * Transform the given RectangularShape according to the given
     * AffineTransform.
     * 
     * @param shape given RectangularShape
     * @param trans given AffineTransform
     * @return transformed RectangularShape
     */
    public static RectangularShape createTransformedShape(RectangularShape shape, AffineTransform trans) {
        RectangularShape newShape = (RectangularShape) shape.clone();
        Rectangle2D bounds = new Rectangle2D.Double(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
        Shape transformedBounds = trans.createTransformedShape(bounds);
        Rectangle2D newBounds = transformedBounds.getBounds2D();
        newShape.setFrame(newBounds);
        return newShape;
    }
}
