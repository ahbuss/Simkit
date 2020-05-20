package simkit.test;
import simkit.random.CongruentialSeeds;
import simkit.random.RandomNumber;
import simkit.random.RandomNumberFactory;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
/**
 *
 * @author  ahbuss
 */
public class TestConvolutionVariate {
    
    public static void main(String[] args) {
        
        RandomVariate[] rvarray = new RandomVariate[3];
        rvarray[0] = RandomVariateFactory.getInstance("Exponential", 1.7);

        rvarray[1] = RandomVariateFactory.getInstance("Gamma", 2.3, 4.5);

        rvarray[2] = RandomVariateFactory.getInstance("Beta", 1.2, 5.6);
            
        RandomNumber rand = RandomNumberFactory.getInstance(CongruentialSeeds.SEED [4]);  
        
        RandomVariate rv = RandomVariateFactory.getInstance("Convolution", rand, 
             new Object[] {rvarray} );
        
        System.out.println(rv);
        
        System.out.println(rv.generate());
        
        rvarray[0] = RandomVariateFactory.getInstance("Constant", 1.0);
        rvarray[1] = RandomVariateFactory.getInstance("Constant", 2.0);
        rvarray[2] = RandomVariateFactory.getInstance("Constant", 3.2);
        
        rv = RandomVariateFactory.getInstance("Convolution", new Object[] {rvarray} );
        
        System.out.println(rv);
        
        System.out.println(rv.generate());
    }
    
}
