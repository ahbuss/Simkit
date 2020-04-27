package simkit.random;

/**
 * 
 * @author ahbuss
 */
public interface RandomObjectVariate<T> extends RandomVariate {
    public T generateObject();
}