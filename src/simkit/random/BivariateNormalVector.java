package simkit.random;

/**
 * Generates bivariate normal vectors with given means, standard deviations, and
 * correlation.
 *
 * @author ahbuss
 */
public class BivariateNormalVector implements RandomVector {

    private RandomVariate[] standardNormalsGenerator;

    private double[] mean;

    private double[] standardDeviation;

    private double correlation;

    private double correlation2;

    /**
     * Instantiate a BivariateNormalVector; Instantiate the
     * standardNormalsGenerator array that generates standard normal (0, 1)
     * variates.
     */
    public BivariateNormalVector() {
        this.setStandardNormalsGenerator(new RandomVariate[]{
            RandomVariateFactory.getInstance("Normal", 0.0, 1.0),
            RandomVariateFactory.getInstance("Normal", 0.0, 1.0)
        });
    }

    /**
     * Uses formula:<br>
     * X_0 = \u03C3<sub>0</sub> * Z<sub>0</sub> + \u03BC<sub>0</sub> <br>
     * X_1 = \u03C3<sub>1</sub> * (\u03C1 * Z<sub>0</sub> +
     * &radic;<span style="text-decoration: overline">1 -
     * \u03C1<sup>2</sup></span> * Z<sub>1</sub>) + \u03BC<sub>1</sub>
     *
     * @return generated bivariate normal vector
     */
    @Override
    public double[] generate() {

        double[] standardNormals = new double[]{
            standardNormalsGenerator[0].generate(),
            standardNormalsGenerator[1].generate()
        };

        double[] correlated = new double[2];

        correlated[0] = standardDeviation[0] * standardNormals[0] + mean[0];
        correlated[1] = standardDeviation[1] * (correlation * standardNormals[0]
                + correlation2 * standardNormals[1]) + mean[1];

        return correlated;
    }

    /**
     *
     * @return copy of mean vector
     */
    public double[] getMean() {
        return mean.clone();
    }

    /**
     * 
     * @param index index of mean - must be 0 or 1
     * @return mean of index
     * @throws ArrayIndexOutOfBoundsException if index is not 0 or 1
     */
    public double getMean(int index) {
        return mean[index];
    }
    /**
     *
     * @param mean vector of means
     * @throws IllegalArgumentException is length of array is not 2
     */
    public void setMean(double[] mean) {
        if (mean.length != 2) {
            throw new IllegalArgumentException(
                    "Array of means must be length 2: " + mean.length);
        }
        this.mean = mean.clone();
    }

    /**
     * 
     * @param index index of mean to set
     * @param mean value of new mean
     * @throws ArrayIndexOutOfBoundsException if index is not 0 or 1
     */
    public void setMean(int index, double mean) {
        this.mean[index] = mean;
    }
    
    /**
     *
     * @return copy of standard deviation vector
     */
    public double[] getStandardDeviation() {
        return standardDeviation.clone();
    }

    /**
     * 
     * @param index index of standardDeviation - must be 0 or 1
     * @return standard deviation of index
     * @throws ArrayIndexOutOfBoundsException if index is not 0 or 1
     */
    public double getStandardDeviation(int index) {
        return standardDeviation[index];
    }
    /**
     *
     * @param standardDeviation standard deviation vector
     * @throws IllegalArgumentException if length of argument array is not 2
     * @throws IllegalArgumentException if either value is &lt; 0.0
     */
    public void setStandardDeviation(double[] standardDeviation) {
        if (standardDeviation.length != 2) {
            throw new IllegalArgumentException("Must have array of length 2: "
                    + standardDeviation.length);
        }
        if (standardDeviation[0] < 0.0 || standardDeviation[1] < 0.0) {
            throw new IllegalArgumentException(
                    String.format("Standard deviations must be \u2265 0.0: "
                            + "(%f, %f)",
                            standardDeviation[0], standardDeviation[1])
            );
        }
        this.standardDeviation = standardDeviation.clone();
    }

    /**
     * 
     * @param index index of standard deviation to set; must be 0 or 1
     * @param standardDeviation new value of standardDeviation[index]
     * @throws ArrayIndexOutOfBoundsException if index is not 0 or 1
     * @throws IllegalArgumentException if either value is &lt; 0.0
     */
    public void setStandardDeviation(int index, double standardDeviation) {
        if (standardDeviation < 0.0) {
            throw new IllegalArgumentException(
                    String.format("Standard deviation must be \u2265 0.0: "
                            + "%f",
                            standardDeviation)
            );
        }
        this.standardDeviation[index] = standardDeviation;
    }
    
    /**
     *
     * @return correlation
     */
    public double getCorrelation() {
        return correlation;
    }

    /**
     *
     * @param correlation desired correlation
     * @throws IllegalArgumentException is argument is not between -1.0 and 1.0
     * inclusive
     */
    public void setCorrelation(double correlation) {
        if (correlation < -1.0 || correlation > 1.0) {
            throw new IllegalArgumentException(
                    "correlation must be between -1.0 and 1.0: "
                    + correlation);
        }
        this.correlation = correlation;
        correlation2 = Math.sqrt(1.0 - correlation * correlation);
    }

    @Override
    public String toString() {
        return String.format("BivariateNormal \u03BC=(%.3f, %.3f) "
                + "\u03C3=(%.3f, %.3f) \u03C1=%.3f",
                mean[0], mean[1], standardDeviation[0], standardDeviation[1],
                correlation);
    }

    @Override
    public void setRandomNumber(RandomNumber randomNumber) {
        standardNormalsGenerator[0].setRandomNumber(randomNumber);
        standardNormalsGenerator[1].setRandomNumber(randomNumber);
    }

    @Override
    public RandomNumber getRandomNumber() {
        return standardNormalsGenerator[0].getRandomNumber();
    }

    /**
     * If array of length 3 is passed, the elements must be double[], double[],
     * double representing the means, variances, and correlation, respectively.
     * <p>
     * If an array of length 5 is passed, the elements must be all doubles,
     * representing the two means, two standardNormals deviations, and
     * correlation.
     *
     * @param parameters means, variances, and correlation
     * @throws IllegalArgumentException if the number of arguments is not 3 or 5
     */
    @Override
    public void setParameters(Object... parameters) {
        if (parameters.length == 3) {
            if ((parameters[0] instanceof double[])
                    && (parameters[1] instanceof double[])
                    && (parameters[2] instanceof Double)) {
                this.setMean((double[]) parameters[0]);
                this.setStandardDeviation((double[]) parameters[1]);
                this.setCorrelation((Double) parameters[2]);
            } else {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < parameters.length; ++i) {
                    builder.append(parameters[i].getClass().getName());
                    if (i < parameters.length - 1) {
                        builder.append(", ");
                    }
                }
                throw new IllegalArgumentException(
                        "3 parameters must be double[], double[], double: "
                        + builder);
            }
        } else if (parameters.length == 5) {
            boolean okay = true;
            for (int i = 0; i < parameters.length; ++i) {
                okay &= (parameters[i] instanceof Double);
            }
            if (okay) {
                double[] means = new double[2];
                means[0] = (Double) parameters[0];
                means[1] = (Double) parameters[1];
                this.setMean(means);

                double[] variances = new double[2];
                variances[0] = (Double) parameters[2];
                variances[1] = (Double) parameters[3];
                this.setStandardDeviation(variances);

                this.setCorrelation((Double) parameters[4]);

            } else {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < parameters.length; ++i) {
                    builder.append(parameters[0].getClass().getName());
                    if (i < parameters.length - 1) {
                        builder.append(", ");
                    }
                }
                throw new IllegalArgumentException(
                        "5 parameters must be all doubles: " + builder);
            }
        } else {
            throw new IllegalArgumentException(
                    "parameters must have length 3 or 5: " + parameters.length);
        }
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{mean[0], mean[1], standardDeviation[0], standardDeviation[1], correlation};
    }

    /**
     *
     * @return shallow copy of standard normal generators
     */
    public RandomVariate[] getStandardNormalsGenerator() {
        return standardNormalsGenerator.clone();
    }

    /**
     * It is assumed that the RandomVariates both generate standard normal
     * variates
     *
     * @param standardNormalsGenerator
     */
    public void setStandardNormalsGenerator(RandomVariate[] standardNormalsGenerator) {
        if (standardNormalsGenerator.length != 2) {
            throw new IllegalArgumentException("Need array of length 2: "
                    + standardNormalsGenerator.length);
        }
        this.standardNormalsGenerator = standardNormalsGenerator.clone();
    }
}
