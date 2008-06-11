package simkit.random;

/**
 * @version $Id$
 * @author ahbuss
 */
public interface RandomObjectVariate<T> extends RandomVariate {
    public T generateObject();
}