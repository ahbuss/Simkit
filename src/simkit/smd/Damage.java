package simkit.smd;

/**
 * Contains information about damage to a Target by a Munition.
 * The type of Object returned by <CODE>getDamage()</CODE> is implementation
 * dependent as is how the Object affects the Target. The specific type is 
 * given by the parameter &lt;T&gt;
 * @author  Arnold Buss
 * 
 * @param <T> The class that Damage wraps
 */
public interface Damage<T> {
    
    /**
     * 
     * @return the damage information
     */
    public T getDamage();

}

