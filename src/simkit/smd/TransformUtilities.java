package simkit.smd;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

/**
 * Contains a method to transform the coordinates of a rectangle.
 *
 * @author Arnold Buss
 * @version $Id: TransformUtilities.java 1083 2008-06-11 20:41:21Z kastork $
 */
public class TransformUtilities {

    /**
     * Transform the given RectangularShape according to the given
     * AffineTransform.
     *
     * @param shape Given RectangularShape
     * @param trans Given AffineTransform
     * @return The transformed RectangularShape
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
