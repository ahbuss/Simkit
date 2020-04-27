package simkit;

/**
 * @author ahbuss
 */
public class Priority implements Comparable<Priority> {

    public static final Priority LOWEST = new Priority("LOWEST", -Double.MAX_VALUE);
    public static final Priority LOWER = new Priority("LOWER", -20.0);
    public static final Priority LOW = new Priority("LOW", -10.0);
    public static final Priority DEFAULT = new Priority("DEFAULT", 0.0);
    public static final Priority HIGH = new Priority("HIGH", 10.0);
    public static final Priority HIGHER = new Priority("HIGHER", 20.0);
    public static final Priority HIGHEST = new Priority("HIGHEST", Double.MAX_VALUE);

    private final double priority;

    private final String name;

    public Priority(String name, double priority) {
        this.name = name;
        this.priority = priority;
    }

    public double getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return String.format("Priority %s [%,.4f]", name, priority);
    }

    @Override
    public int compareTo(Priority other) {
        if (this.getPriority() > other.getPriority()) {
            return -1;
        }
        if (this.getPriority() < other.getPriority()) {
            return 1;
        }
        return 0;
    }
}
