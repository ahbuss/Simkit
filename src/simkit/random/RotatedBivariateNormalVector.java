package simkit.random;

/**
 *
 * @author ahbuss
 */
public class RotatedBivariateNormalVector implements RandomVector {

    private RandomNumber rng;
    
    private double[] mean;
    
    private double[] standardDeviation;
    
    private double angle;
    
    private Normal02Variate[] standardNormalsGenerator;
    
    private double cosAngle;
    
    private double sinAngle;
    
    public RotatedBivariateNormalVector() {
        this.standardNormalsGenerator = new Normal02Variate[2];
        this.standardNormalsGenerator[0] = (Normal02Variate)RandomVariateFactory.getInstance("Normal02", 0.0, 1.0);
        this.standardNormalsGenerator[1] = (Normal02Variate)RandomVariateFactory.getInstance("Normal02", 0.0, 1.0);
        this.setMean(new double[] { 0.0, 0.0 });
        this.setStandardDeviation(new double[] { 1.0, 1.0 });
        this.setAngle(0.0);
    }
    
    @Override
    public double[] generate() {
        double[] standard = new double[] {
        this.standardNormalsGenerator[0].generate(),
        this.standardNormalsGenerator[1].generate()};

        double[] rotated = new double[] {
            cosAngle * standard[0] - sinAngle * standard[1] + getMean(0),
            sinAngle * standard[0] + cosAngle * standard[1] + getMean(1)
        };
        return rotated;
    }

    @Override
    public void setParameters(Object... params) {
        switch(params.length) {
            case 0:
                break;
            case 1: // Standard Normal; params[0] is angle
                checkParameters(params);
                this.setAngle((Double)params[0]);
                break;
            case 2: // params are standard deviations
                checkParameters(params);
                this.setStandardDeviation(0, (Double)params[0]);
                this.setStandardDeviation(1, (Double)params[1]);
                break;
            case 3: // params are standard deviations and angle
                checkParameters(params);
                this.setStandardDeviation(0, (Double)params[0]);
                this.setStandardDeviation(1, (Double)params[1]);
                this.setAngle((Double)params[2]);
                break;
            case 4: // params are means and standard deviations
                checkParameters(params);
                this.setMean(0, (Double)params[0]);
                this.setMean(1, (Double)params[1]);
                this.setStandardDeviation(0, (Double)params[2]);
                this.setStandardDeviation(1, (Double)params[3]);
                break;
            case 5: // params are means, standard deviations, and angle
                this.setMean(0, (Double)params[0]);
                this.setMean(1, (Double)params[1]);
                this.setStandardDeviation(0, (Double)params[2]);
                this.setStandardDeviation(1, (Double)params[3]);
                this.setAngle((Double)params[4]);
                break;
            default:
                throw new IllegalArgumentException("params length must be between 0 and 5: " +
                        params.length);
        }
    }
    
    private boolean checkParameters(Object... params) {
        boolean okay = true;
        for (int i = 0; i < params.length; ++i) {
            if (!(params[i] instanceof Number)) {
                okay = false;
                break;
            }
        }
        return okay;
    }

    @Override
    public Object[] getParameters() {
        return new Object[] { this.mean[0], this.mean[1], this.standardDeviation[0],
            this.standardDeviation[1], this.angle};
    }

    @Override
    public void setRandomNumber(RandomNumber rng) {
        this.rng = rng;
    }

    @Override
    public RandomNumber getRandomNumber() {
        return this.rng;
    }

    /**
     * @return the mean
     */
    public double[] getMean() {
        return mean;
    }
    
    public double getMean(int index) {
        return this.mean[index];
    }

    /**
     * @param mean the mean to set
     */
    public void setMean(double[] mean) {
        if (mean.length != 2) {
            throw new IllegalArgumentException("mean array must be length 2: " +
                    mean.length);
        }
        if (this.mean == null) {
            this.mean = new double[2];
        }
        this.setMean(0, mean[0]);
        this.setMean(1, mean[1]);
    }

    public void setMean(int index, double mean) {
        this.mean[index] = mean;
        this.standardNormalsGenerator[index].setMean(mean);
    }
    
    /**
     * @return the standardDeviation
     */
    public double[] getStandardDeviation() {
        return standardDeviation.clone();
    }
    
    public double getStandardDeviation(int index) {
        return this.standardDeviation[index];
    }

    /**
     * @param standardDeviation the standardDeviation to set
     */
    public void setStandardDeviation(double[] standardDeviation) {
        if (standardDeviation.length != 2) {
            throw new IllegalArgumentException("standardDeviation array must be length 2: " +
                    standardDeviation.length);
        }
        if (this.standardDeviation == null) {
            this.standardDeviation = new double[2];
        }
        if (standardDeviation[0] < 0.0 || standardDeviation[1] < 0.0) {
            throw new IllegalArgumentException(
                    String.format("standardDeviations must be \uu2265 0.0: [%f, %f]",
                            standardDeviation[0], standardDeviation[1]));
        }
        this.setStandardDeviation(0, standardDeviation[0]);
        this.setStandardDeviation(1, standardDeviation[1]);
    }
    
    public void setStandardDeviation(int index, double standardDeviation) {
        if (index  > 1 || index < 0) {
            throw new IllegalArgumentException("index must be 0 or 1: " +
                    index);
        }
        if (standardDeviation < 0.0 ) {
            throw new IllegalArgumentException(
                    String.format("standardDeviation[%d] must be \uu2265 0.0: %f",
                            index, standardDeviation));
        }
        this.standardDeviation[index] = standardDeviation;
        standardNormalsGenerator[index].setStandardDeviation(standardDeviation);
    }

    /**
     * @return the angle
     */
    public double getAngle() {
        return angle;
    }

    /**
     * @param angle the angle to set
     */
    public void setAngle(double angle) {
        this.angle = angle;
        this.cosAngle = Math.cos(angle);
        this.sinAngle = Math.sin(angle);
    }
    
    @Override
    public String toString() {
        return String.format("Rotated Bivariate Normal (%.3f, %.3f, %.3f, %.3f, %.3f)",
                getMean(0), getMean(1), getStandardDeviation(0), getStandardDeviation(1),
                getAngle());
    }

}
