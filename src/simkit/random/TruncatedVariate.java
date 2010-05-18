package simkit.random;

/**
 * Generates the maximum of a RandomVariate and a truncation point.
 *
 * @version $Id$
 * @author ahbuss
 */
public class TruncatedVariate extends RandomVariateBase {

    private RandomVariate randomVariate;
    private double truncationPoint;

    /**
     *
     * @return max(truncationPoint, randomVariate,generate())
     */
    @Override
    public double generate() {
        return Math.max(truncationPoint, randomVariate.generate());
    }

    @Override
    public void setParameters(Object... params) {
        if (params.length != 2) {
            throw new IllegalArgumentException(
                    "Requires 2 arguments: " + params.length);
        }
        if (params[0] instanceof RandomVariate && params[1] instanceof Number) {
            setRandomVariate((RandomVariate) params[0]);
            this.randomVariate.setRandomNumber(rng);
            setTruncationPoint(((Number) params[1]).doubleValue());
        } else {
            throw new IllegalArgumentException(
                    "Requires (RandomVariate, double) - given ("
                    + params[0].getClass().getName() + ","
                    + params[1].getClass().getName() + ")");
        }
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{getRandomVariate(), getTruncationPoint()};
    }

    /**
     * @return the randomVariate
     */
    public RandomVariate getRandomVariate() {
        return randomVariate;
    }

    /**
     * @param randomVariate the randomVariate to set
     */
    public void setRandomVariate(RandomVariate randomVariate) {
        this.randomVariate = randomVariate;
    }

    /**
     * @return the truncationPoint
     */
    public double getTruncationPoint() {
        return truncationPoint;
    }

    /**
     * @param truncationPoint the truncationPoint to set
     */
    public void setTruncationPoint(double truncationPoint) {
        this.truncationPoint = truncationPoint;
    }

    public String toString() {
        return randomVariate.toString() + " (Truncated at " + getTruncationPoint() + ")";
    }
}
