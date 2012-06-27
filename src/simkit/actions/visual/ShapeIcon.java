/*
 * ShapeIcon.java
 *
 * Created on March 10, 2002, 5:19 PM
 */

package simkit.actions.visual;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

/**
 *
 * @author  Arnold Buss
 * @version
 */
public class ShapeIcon implements Icon {
    
    protected static final Color DEFAULT_OUTLINE_COLOR = Color.black;
    protected static final Color DEFAULT_FILL_COLOR = Color.white;
    protected static final float DEFAULT_STROKE_WIDTH = 2.0f;
    
    protected Shape shape;
    protected Color fillColor;
    protected Color outlineColor;
    protected Stroke stroke;
    protected AffineTransform transform;
    protected boolean filled;
    
    
    public ShapeIcon(Shape shape, Color outlineColor, Color fillColor, boolean filled) {
        this(shape, outlineColor, fillColor);
        setFilled(filled);
    }
    
    public ShapeIcon(Shape shape, Color outlineColor, boolean filled) {
        this(shape, outlineColor);
        setFilled(filled);
    }
    
    public ShapeIcon(Shape shape, Color outlineColor, Color fillColor) {
        this.shape = shape;  // unsafe - how to clone safely?
        setOutlineColor(outlineColor);
        setFillColor(fillColor);
        setStroke(new BasicStroke(DEFAULT_STROKE_WIDTH));
        setTransform(new AffineTransform());
        setFilled(true);
    }
    
    public ShapeIcon(Shape shape, Color outlineColor) {
        this(shape, outlineColor, DEFAULT_FILL_COLOR);
    }
    
    public ShapeIcon(Shape shape) {
        this(shape, DEFAULT_OUTLINE_COLOR);
    }
    
    public ShapeIcon(Shape shape, boolean filled) {
        this(shape);
        setFilled(filled);
    }
    
    public int getIconWidth() {
        return (int) shape.getBounds2D().getWidth();
    }
    
    public int getIconHeight() {
        return (int) shape.getBounds2D().getHeight();
    }
    
    public void paintIcon(Component component, Graphics graphics, int x, int y) {
        Shape transformedShape = transform.createTransformedShape(shape);
        Graphics2D g2d = (Graphics2D) graphics;
        AffineTransform trans = AffineTransform.getTranslateInstance(x, y);
        transformedShape = trans.createTransformedShape(transformedShape);
        g2d.setColor(component.getForeground());
        if (isFilled()) {
            g2d.setColor(fillColor);
            g2d.fill(transformedShape);
        }
        if (isOutlined()) {
            g2d.setColor(outlineColor);
            g2d.setStroke(stroke);
            g2d.draw(transformedShape);
        }
    }
    
    
    public void setTransform(AffineTransform t) { 
        transform = t != null ? new AffineTransform(t) : new AffineTransform(); 
    }
    
    public AffineTransform getTransform() { return new AffineTransform(transform); }
    
   // Note: find a generic way to clone Shape
    public Shape getShape() { return shape; }
    
    public void setFillColor(Color color) { fillColor = color; }
    
    public Color getFillColor() { return fillColor; }
    
    public void setOutlineColor(Color color) { outlineColor = color; }
    
    public void setStroke(Stroke newStroke) { stroke = newStroke; }
    
    public void setFilled(boolean b) { filled = b; }
    
    public boolean isFilled() { return filled; }
    
    public boolean isOutlined() { return outlineColor != null; }
    
    public String toString() {
        return "ShapeIcon: Shape=" + shape + " outlineColor=" + outlineColor +
        (isFilled() ? " fillColor=" + fillColor : "");
    }
    
}
