package simkit.random;

/**
 *
 * @author  Arnold Buss
 */
public interface Pooled extends RandomNumber {
    
    public void setFirst(RandomNumber first);
    
    public RandomNumber getFirst();
    
    public void setSecond(RandomNumber first);
    
    public RandomNumber getSecond();
    
}
