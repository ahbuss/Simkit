package simkit.random;

public class NormalVariate extends RandomVariateBase {

    private double savedValue;
    private boolean hasSavedValue;

    private double mean;
    private double sigma;

    public NormalVariate() {
    }

    public void setParameters(Object[] params) {

        if (params.length != 2) {
            throw new IllegalArgumentException("Need (mean, std. dev.), received " +
                params.length + " parameters");
        }

        super.setParameters(params);
        double temp = ((Number) params[1]).doubleValue();
        if (temp <= 0.0) {
            throw new IllegalArgumentException("Need std. dev. > 0: " + sigma);
        }
        mean = ((Number) params[0]).doubleValue();
        sigma = temp;
    }

    public double generate() {
        double value = Double.NaN;
        if (hasSavedValue) {
            value = savedValue;
            hasSavedValue = false;
        }
        else {
            double w;
            double v1;
            double v2;
            do {
                v1 = 2.0 * rng.draw() - 1.0;
                v2 = 2.0 * rng.draw() - 1.0;
                w = v1 * v1 + v2 * v2;
            } while (w > 1.0);
            double y = Math.sqrt(-2.0 * Math.log(w) / w);
            value = v1 * y;
            savedValue = v2 * y;
            hasSavedValue = true;
        }
        return value * sigma + mean;
    }

    public String toString() { return "Normal (" + mean + ", " + sigma + ")"; }
}
