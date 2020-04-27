/*
 * TriangleVariate.java
 *
 * Created on April 13, 2002, 2:25 PM
 */
package simkit.random;

import java.util.Arrays;

/**
 * Generates Triangle random variates.
 *
 * @author Arnold Buss
 * 
 */
public class TriangleVariate extends RandomVariateBase implements RandomVariate {

    /**
     * The left most or smallest value. (a)
*
     */
    private double left;

    /**
     * The right most or largest value. (b)
*
     */
    private double right;

    /**
     * The peak of the triangle. (c)
*
     */
    private double center;

    private double centerMinusLeft;
    private double rightMinusCenter;
    private double centerMinusLeftOverRightMinusLeft;

    private boolean needsValidation;

    /**
     * Creates a new TriangleVariate. The parameters must be set prior to use.
     */
    public TriangleVariate() {
    }

    /**
     * 
     * @return a 3 element array containing the left, right, and center values
     * as Doubles.
     */
    public Object[] getParameters() {
        return new Object[]{left, right, center};
    }

    /**
     * Sets the left, right, and center points of this RandomVariate.
     *
     * @param params A three element array containing the left, right, and
     * center values as Numbers.
     * @throws IllegalArgumentException If the array does not contain exactly 3
     * elements, or if all three are not Numbers
     *
     */
    @Override
    public void setParameters(Object... params) {
        if (params == null) {
            throw new NullPointerException();
        }
        if (params.length != 3) {
            throw new IllegalArgumentException("TriangleVariate needs 3 parameters: " + params.length);
        }
        if (params[0] instanceof Number && params[1] instanceof Number && params[2] instanceof Number) {
            setLeft(((Number) params[0]).doubleValue());
            setRight(((Number) params[1]).doubleValue());
            setCenter(((Number) params[2]).doubleValue());
            if (!validate()) {
                throw new IllegalArgumentException(
                        "TriangleVariate needs a <= c <= b: ("
                        + left + ", " + right + ", " + center + ")"
                );
            }
        } else {
            throw new IllegalArgumentException(
                    "TriangleVariate requires (Number, Number, Number): ("
                    + params[0].getClass().getName() + ", "
                    + params[1].getClass().getName() + ", "
                    + params[2].getClass().getName() + ")"
            );
        }
    }

    /**
     * 
     * @param a smallest value
     */
    public void setLeft(double a) {
        left = a;
        needsValidation = true;
    }

    /**
     * 
     * @return smallest value
     */
    public double getLeft() {
        return left;
    }

    /**
     * 
     * @param b largest value
     */
    public void setRight(double b) {
        right = b;
        needsValidation = true;
    }

    /**
     * 
     * @return largest value
     */
    public double getRight() {
        return right;
    }

    /**
     * 
     * @param c median value
     */
    public void setCenter(double c) {
        center = c;
        needsValidation = true;
    }

    /**
     * 
     * @return median value
     */
    public double getCenter() {
        return center;
    }

    /**
     * Verifies the 3 parameters are consistent and does some precalculations.
     * Must be called after changing any parameter prior to generating.
     * <br><i>Note:</i> this now sorts the parameters and puts them in the
     * "right" order regardless of how they were passed in.
     * @return true if 3 parameters are consistent
     *
     */
    private boolean validate() {
        double[] params = new double[]{left, center, right};
        Arrays.sort(params);
        left = params[0];
        center = params[1];
        right = params[2];
//
//        boolean valid = left <= center && center <= right;
//        if (valid) {
        centerMinusLeft = center - left;
        rightMinusCenter = right - center;
        centerMinusLeftOverRightMinusLeft = (center - left) / (right - left);
//        }
//        return valid;
        return true;
    }

    /**
     * Generate a random variate having this class's distribution.
     *
     * @return The generated random variate
     */
    @Override
    public double generate() {
        if (needsValidation) {
            needsValidation = !validate();
        }
        double u = rng.draw();
        double v = rng.draw();
        double w = rng.draw();

        if (u < centerMinusLeftOverRightMinusLeft) {
            return left + centerMinusLeft * Math.max(v, w);
        } else {
            return center + rightMinusCenter * Math.min(v, w);
        }
    }

    @Override
    public String toString() {
        return "Triangle (" + left + ", " + right + ", " + center + ")";
    }
}
