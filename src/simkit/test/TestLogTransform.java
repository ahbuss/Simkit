package simkit.test;
import simkit.random.*;
/**
 *
 * @author  ahbuss
 */
public class TestLogTransform {
    
    public static void main(String[] args) {
        RandomVariate rv = RandomVariateFactory.getInstance("Exponential", 1.2);

        System.out.println(rv);
        RandomVariate logRV = RandomVariateFactory.getInstance(
            "LogTransform", rv );
        
        System.out.println(logRV);
        
        for (int i = 0; i < 10; ++i) {
            System.out.println(logRV.generate());
        }
        
        RandomVariate norm = RandomVariateFactory.getInstance(
            "Normal", 0.0, 2.0 );
        
        ((LogTransform) logRV).setRandomVariate(norm);
        System.out.println(logRV);
        
        for (int i = 0; i < 20; ++i) {
            System.out.println(logRV.generate());
        }
    }
    
}
