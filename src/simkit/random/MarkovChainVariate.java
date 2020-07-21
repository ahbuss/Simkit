package simkit.random;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * <p>
 * Instances of this class generate values from a Markov Chain with the given
 * transition probabilities on the state space [0,..n-1] where n is the size of
 * the transition matrix.
 * <p>
 * IMPORTANT: if the initial initialState is not specified, then it defaults to
 * 0. If this is not a valid initialState, then it should be explicitly called
 * or specified in the call to <code>RandomVariateFactory</code>.
 *
 * @author ahbuss
 */
public class MarkovChainVariate extends RandomVariateBase implements DiscreteRandomVariate {

    private static final Logger LOGGER = Logger.getLogger(MarkovChainVariate.class.getName());

    private static final double EPSILON = 1.0E-10;

    private double[][] transitionMatrix;

    private final Map<Integer, DiscreteRandomVariate> transitions;

    private int initialState;

    protected int currentState;

    public MarkovChainVariate() {
        transitions = new TreeMap<>();
    }

    @Override
    public double generate() {
        return (double) generateInt();
    }

    /**
     * <p>
     * The first argument should be the transition matrix as a
     * <code>double[][]</code>. Because of the peculiarities of varargs, it must
     * be wrapped in an <code>Object[]<code> array.
     * <p>
     * The second (optional) argumen is an integer that is the initial
     * initialState.
     *
     * @param params Given parameters
     * @throws IllegalArgumentException is params.length is not 1 or 2
     * @throws IllegalArgumentException if params[0] is not a
     * <code>double[][]</code> array
     * @throws IllegalArgumentException if params.length == 2 and params[1] is
     * not an Integer
     */
    @Override
    public void setParameters(Object... params) {
        String message = "";
        switch (params.length) {
            case 1:
                initialState = 0;
                if (params[0] instanceof double[][]) {
                    setTransitionMatrix((double[][]) params[0]);
                } else {
                    message += ("params[0] must be double[][]: " + params[0].getClass().getSimpleName());
                }
                break;
            case 2:
                if (params[0] instanceof double[][]) {
                    setTransitionMatrix((double[][]) params[0]);
                } else {
                    message += ("params[0] must be double[][]: " + params[0].getClass().getSimpleName());
                }
                if (params[1] instanceof Integer) {
                    this.setInitialState((int) params[1]);
                } else {
                    message += (" params[1] if present must be Integer: " + params[1].getClass().getSimpleName());
                }
                break;
            default:
                message += ("params must be length 0 or 2: " + params.length);
                break;
        }
        currentState = initialState;
        if (message.length() > 0) {
            LOGGER.severe(message);
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public Object[] getParameters() {
        Object[] params = new Object[]{
            deepCopy(transitionMatrix), getInitialState()};
        return params;
    }

    /**
     *
     * @return value generated from currentState and transition matrix
     */
    @Override
    public int generateInt() {
        DiscreteRandomVariate drv = transitions.get(currentState);
        this.currentState = drv.generateInt();
        return currentState;
    }

    /**
     * @return a deep copy of the transitionMatrix
     */
    public double[][] getTransitionMatrix() {
        return deepCopy(transitionMatrix);
    }

    /**
     * This normalizes the given transitionMatrix so that each row is a discrete
     * probability distribution.
     *
     * @param transitionMatrix the transitionMatrix to set
     * @throws IllegalArgumentException if given transitionMatrix is not square
     *
     */
    public void setTransitionMatrix(double[][] transitionMatrix) {
        if (!isSquare(transitionMatrix)) {
            String message = "transition matrix muxt be square";
            LOGGER.severe(message);
            throw new IllegalArgumentException(message);
        }
        this.transitionMatrix = deepCopy(transitionMatrix);
        normalize(this.transitionMatrix);
        int[] states = new int[transitionMatrix.length];
        for (int row = 0; row < states.length; ++row) {
            states[row] = row;
        }
        for (int row = 0; row < transitionMatrix.length; ++row) {
            DiscreteIntegerVariate rv
                    = (DiscreteIntegerVariate) RandomVariateFactory.getDiscreteRandomVariateInstance("DiscreteInteger", states.clone(), transitionMatrix[row]);
            transitions.put(row, rv);
        }
    }

    /**
     * @return the row
     */
    public int getInitialState() {
        return initialState;
    }

    /**
     * @param initialState the row to set
     */
    public void setInitialState(int initialState) {
        this.initialState = initialState;
    }

    /**
     * @return the currentState
     */
    public int getCurrentState() {
        return currentState;
    }

    /**
     *
     * @param matrix Given matrix
     * @return a deep copy of the given matrix
     */
    public static double[][] deepCopy(double[][] matrix) {
        double[][] deepCopy = new double[matrix.length][];
        for (int i = 0; i < deepCopy.length; ++i) {
            deepCopy[i] = matrix[i].clone();
        }
        return deepCopy;
    }

    /**
     * This normalizes the given matrix in-place, so beware. Note that if any
     * rows are all 0.0, that row will remain all zeros.
     *
     * @param matrix Given matrix
     * @throws IllegalArgumentException if it finds an entry that is negative
     */
    public static void normalize(double[][] matrix) {
        for (int row = 0; row < matrix.length; ++row) {
            double sum = 0.0;
            for (int col = 0; col < matrix[row].length; ++col) {
                if (matrix[row][col] < 0.0) {
                    String message = String.format(
                            "Negative value in transition matrix: P[%,d,%,d]=%,.3f",
                            row, col, matrix[row][col]);
                    LOGGER.severe(message);
                    throw new IllegalArgumentException(message);
                }
                sum += matrix[row][col];
            }
            for (int col = 0; col < matrix[row].length; ++col) {
                if (sum > 0.0) {
                    matrix[row][col] /= sum;
                }
            }
        }
    }

    /**
     * '
     *
     * @param matrix Given double[][] array
     * @return true if given array is square, false otherwise
     */
    public static boolean isSquare(double[][] matrix) {
        boolean square = true;
        for (int row = 0; row < matrix.length; ++row) {
            if (matrix[row].length != matrix.length) {
                square = false;
                break;
            }
        }
        return square;
    }

    /**
     * This uses the power approach to finding the steady-state distribution.
     * The given matrix is first normalized, then successively multiplied by
     * itself until the total "score" is less than EPSILON (= 1.oE-10). The
     * score is computed as the sum of the differences between the maximum and
     * minimum values of the columns.
     *
     * @param matrix Given matrix
     * @return Steady-state distribution
     */
    public static double[] getSteadyState(double[][] matrix) {
        double[] steadyState = new double[matrix.length];
        if (isSquare(matrix)) {
            double[][] copy = deepCopy(matrix);
            normalize(copy);
            double copyScore = score(copy);
            while (copyScore > EPSILON) {
                copy = matrixSquare(copy);
                copyScore = score(copy);
            }
            steadyState = copy[0];
        }
        return steadyState;
    }

    /**
     *
     * @param matrix Given double[][] array
     * @return the given array matrix-multiplied by itself or itself if not
     * square
     */
    public static double[][] matrixSquare(double[][] matrix) {
        double[][] matrixSq = new double[matrix.length][matrix[0].length];
        if (isSquare(matrix)) {
            for (int i = 0; i < matrix.length; ++i) {
                for (int j = 0; j < matrix.length; ++j) {
                    double sum = 0.0;
                    for (int k = 0; k < matrix.length; ++k) {
                        sum += matrix[i][k] * matrix[k][j];
                    }
                    matrixSq[i][j] = sum;
                }
            }
        } else {
            matrixSq = matrix;
        }
        return matrixSq;
    }

    /**
     * The score is the sum of the max - min of each column
     *
     * @param matrix Given double[][] array
     * @return sum of max - min over columns
     */
    public static double score(double[][] matrix) {
        double score = 0.0;
        for (int col = 0; col < matrix.length; ++col) {
            double max = Double.NEGATIVE_INFINITY;
            double min = Double.POSITIVE_INFINITY;
            for (int row = 0; row < matrix.length; ++row) {
                if (matrix[row][col] < min) {
                    min = matrix[row][col];
                }
                if (matrix[row][col] > max) {
                    max = matrix[row][col];
                }
            }
            score += max - min;
        }
        return score;
    }

    /**
     *
     * @param matrix Given double[][] array
     * @return String of given matrix
     */
    public static String matrixToString(double[][] matrix) {
        StringBuilder builder = new StringBuilder(Arrays.toString(matrix[0]));
        for (int i = 1; i < matrix.length; ++i) {
            builder.append('\n').append(Arrays.toString(matrix[i]));
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Markov Chain");
        for (int row = 0; row < transitionMatrix.length; ++row) {
            builder.append('\n').append(Arrays.toString(transitionMatrix[row]));
        }
        return builder.toString();
    }

}
