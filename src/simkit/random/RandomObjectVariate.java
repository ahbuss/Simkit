package simkit.random;

/**
 * @version $Id: RandomObjectVariate.java 1051 2008-02-27 00:14:47Z ahbuss $
 * @author ahbuss
 */
public interface RandomObjectVariate<T> extends RandomVariate {
    public T generateObject();
}