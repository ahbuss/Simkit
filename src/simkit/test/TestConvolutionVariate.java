package simkit.test;
import simkit.random.*;
/**
 *
 * @author  ahbuss
 */
public class TestConvolutionVariate {
    
    public static void main(String[] args) {
        
        RandomVariate[] rvarray = new RandomVariate[3];
        rvarray[0] = RandomVariateFactory.getInstance("Exponential",
            new Object[] { new Double(1.7) });
        rvarray[1] = RandomVariateFactory.getInstance("Gamma",
            new Object[] { new Double(2.3), new Double(4.5) });
        rvarray[2] = RandomVariateFactory.getInstance("Beta", 
            new Object[] { new Double(1.2), new Double(5.6) });
            
        RandomNumber rand = RandomNumberFactory.getInstance(CongruentialSeeds.SEED [4]);  
        
        RandomVariate rv = RandomVariateFactory.getInstance("Convolution",
            new Object[] { rvarray }, rand );
        
        System.out.println(rv);
        
        System.out.println(rv.generate());
        
        rvarray[0] = RandomVariateFactory.getInstance("Constant", new Object[] { new Double(1.0) });
        rvarray[1] = RandomVariateFactory.getInstance("Constant", new Object[] { new Double(2.0) });
        rvarray[2] = RandomVariateFactory.getInstance("Constant", new Object[] { new Double(3.2) });
        
        rv = RandomVariateFactory.getInstance("Convolution",
            new Object[] { rvarray } );
        
        System.out.println(rv);
        
        System.out.println(rv.generate());
    }
    
}
