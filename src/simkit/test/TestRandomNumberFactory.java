package simkit.test;
import simkit.random.CongruentialSeeds;
import simkit.random.RandomNumber;
import simkit.random.RandomNumberFactory;
/**
 *
 * @author  Arnold Buss
 */
public class TestRandomNumberFactory {
    
    /**
     * @deprecated now in unit test suite
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Class theClass = RandomNumberFactory.getClassFor("Congruential");
        System.out.println(theClass);        
        System.out.println(RandomNumberFactory.getCache());
        theClass = RandomNumberFactory.getClassFor("simkit.random.PooledGenerator");
        System.out.println(theClass);
        System.out.println(RandomNumberFactory.getCache());
        theClass = RandomNumberFactory.getClassFor("simkit.test.DummyGenerator");
        System.out.println(theClass);
        System.out.println(RandomNumberFactory.getCache());
//        RandomNumberFactory.addSearchPackage("simkit.test");
        theClass = RandomNumberFactory.getClassFor("DummyGenerator");
        System.out.println(theClass);
        System.out.println(RandomNumberFactory.getCache());
        
        theClass = RandomNumberFactory.getClassFor("ExponentialVariate");
        System.out.println(theClass);
        theClass = RandomNumberFactory.getClassFor("TestRandomNumberFactory");
        System.out.println(theClass);
        
        RandomNumberFactory.setDefaultClass("Tausworthe");
        RandomNumber rng1 = RandomNumberFactory.getInstance(CongruentialSeeds.SEED[0]);
        System.out.println(rng1);
        
        RandomNumber rng2 = RandomNumberFactory.getInstance("DummyGenerator");
        System.out.println(rng2);
        
        rng2 = RandomNumberFactory.getInstance("Congruential");
        
        RandomNumber rng3 = RandomNumberFactory.getInstance("PooledXORGenerator",
            rng1, rng2);
        System.out.println(rng3);
    }
    
}
