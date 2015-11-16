package simkit.random;

/**
 * Generates bivariate normal random vectors whose correlation is determined by
 * the rotation rather than directly via &rho; or covariance
 * &sigma;<sub>12</sub>. First, a pair of independent normal(0, &sigma;) random
 * variates are generated, then a rotation is applied based on the angle.
 * <p>
 * See comments in {@link #setParameters(java.lang.Object...)} for options in
 * setting parameters.
 *
 * @version $Id$
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

    /**
     * Instantiate the underlying normals; set means to 0.0 and standard
     * deviations to 1.0, angle to 0.0 by default.
     */
    public RotatedBivariateNormalVector() {
        this.standardNormalsGenerator = new Normal02Variate[2];
        this.standardNormalsGenerator[0] = (Normal02Variate) RandomVariateFactory.getInstance("Normal02", 0.0, 1.0);
        this.standardNormalsGenerator[1] = (Normal02Variate) RandomVariateFactory.getInstance("Normal02", 0.0, 1.0);
        this.setMean(new double[]{0.0, 0.0});
        this.setStandardDeviation(new double[]{1.0, 1.0});
        this.setAngle(0.0);
    }

    /**
     * First generate two independent normals with zero mean and the specified
     * standard deviations. Then apply the rotation matrix and return.
     *
     * @return Bivariate Normal vector rotated about angle &theta;
     */
    @Override
    public double[] generate() {
        double[] standard = new double[]{
            this.standardNormalsGenerator[0].generate(),
            this.standardNormalsGenerator[1].generate()};

        double[] rotated = new double[]{
            cosAngle * standard[0] + sinAngle * standard[1] + getMean(0),
            -sinAngle * standard[0] + cosAngle * standard[1] + getMean(1)
        };
        return rotated;
    }

    /**
     * The number of parameters can be 0-5, with 5 being the "complete" set.
     * They are interpreted as follows (list number is length of params
     * argument):
     *
     * <ol start="0">
     * <li> All default values: &mu; = (0.0, 0.0); &sigma; = (1.0, 1.0); &theta;
     * = 0.0
     * <li> Parameter is angle &theta; = params[0]; rest default: &mu; = (0.0,
     * 0.0); &sigma; = (1.0, 1.0)
     * <li> Parameters are standard deviations, &sigma; = (params[0],
     * params[1]); rest default: &mu; = (0.0, 0.0); &theta; = 0.0
     * <li> Parameters are standard deviations and angle; means are zero:
     * &sigma; = (params[0], params[1]); &theta; = params[2]; &mu; = (0.0, 0.0)
     * <li> Parameters are means and standard deviations; angle is zero: &mu; =
     * (params[0], params[1]); &sigma; = (params[2], params[3]); &theta; = 0.0
     * <li> Full parameter list: &mu; = (params[0], params[1]); &sigma; =
     * (params[2], params[3]); &theta; = params[4]
     * </ol>
     *
     * @param params Input parameters (see comments for options)
     * @throws IllegalArgumentException if any of the parameters are not 
     * instances of Number 
     */
    @Override
    public void setParameters(Object... params) {
        switch (params.length) {
            case 0:
                this.setParameters(0.0, 0.0, 1.0, 1.0, 0.0);
                break;
            case 1: // Standard Normal; params[0] is angle
                checkParameters(params);
                this.setParameters(0.0, 0.0, 1.0, 1.0, params[0]);
                break;
            case 2: // params are standard deviations
                checkParameters(params);
                this.setParameters(0.0, 0.0, params[0], params[1], 0.0);
                break;
            case 3: // params are standard deviations and angle
                checkParameters(params);
                this.setParameters(0.0, 0.0, params[0], params[1], params[2]);
                break;
            case 4: // params are means and standard deviations
                checkParameters(params);
                this.setParameters(params[0], params[1], params[2], params[3], 0.0);
                break;
            case 5: // params are means, standard deviations, and angle
                this.setMean(0, ((Number) params[0]).doubleValue());
                this.setMean(1, ((Number) params[1]).doubleValue());
                this.setStandardDeviation(0, ((Number) params[2]).doubleValue());
                this.setStandardDeviation(1, ((Number) params[3]).doubleValue());
                this.setAngle(((Number) params[4]).doubleValue());
                break;
            default:
                throw new IllegalArgumentException("params length must be between 0 and 5: "
                        + params.length);
        }
    }

    /**
     *
     * @param params Parameters to check
     * @return true if all elements in Object array are instances of Number
     */
    private static boolean checkParameters(Object... params) {
        boolean okay = true;
        for (int i = 0; i < params.length; ++i) {
            if (!(params[i] instanceof Number)) {
                okay = false;
                break;
            }
        }
        return okay;
    }

    /**
     *
     * @return Parameters: {&mu;<sub>0</sub>, &mu;<sub>1</sub>,
     * &sigma;<sub>0</sub>, &sigma;<sub>1</sub>, &theta;}
     */
    @Override
    public Object[] getParameters() {
        return new Object[]{this.mean[0], this.mean[1], this.standardDeviation[0],
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

    /**
     *
     * @param index index of mean to get
     * @return mean[index]
     * @throws ArrayIndexOutOfBoundsException if index &lt; 0 or index &gt; 1
     */
    public double getMean(int index) {
        return this.mean[index];
    }

    /**
     * @param mean the mean to set
     */
    public void setMean(double[] mean) {
        if (mean.length != 2) {
            throw new IllegalArgumentException("mean array must be length 2: "
                    + mean.length);
        }
        if (this.mean == null) {
            this.mean = new double[2];
        }
        this.setMean(0, mean[0]);
        this.setMean(1, mean[1]);
    }

    /**
     *
     * @param index index of mean to set
     * @param mean mean[index]
     * @throws ArrayIndexOutOfBoundsException if index &lt; 0 or index &gt; 1
     */
    public void setMean(int index, double mean) {
        this.mean[index] = mean;
    }

    /**
     * @return the standardDeviation
     */
    public double[] getStandardDeviation() {
        return standardDeviation.clone();
    }

    /**
     *
     * @param index index of standard deviation to get
     * @return standardDeviation[index]
     * @throws ArrayIndexOutOfBoundsException if index &lt; 0 or index &gt; 1
     */
    public double getStandardDeviation(int index) {
        return this.standardDeviation[index];
    }

    /**
     * @param standardDeviation the standardDeviation to set
     * @throws IllegalArgumentException if standardDeviation not of length 2
     * @throws IllegalArgumentException if either value is &lt; 0.0
     */
    public void setStandardDeviation(double[] standardDeviation) {
        if (standardDeviation.length != 2) {
            throw new IllegalArgumentException("standardDeviation array must be length 2: "
                    + standardDeviation.length);
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

    /**
     *
     * @param index index of standard deviation to set
     * @param standardDeviation standardDeviatioon[index]
     * @throws IllegalArgumentException if index &lt; 0 or index &gt; 1.
     * @throws IllegalArgumentException if standardDeviatioon &lt; 0.0
     */
    public void setStandardDeviation(int index, double standardDeviation) {
        if (index > 1 || index < 0) {
            throw new IllegalArgumentException("index must be 0 or 1: "
                    + index);
        }
        if (standardDeviation < 0.0) {
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
