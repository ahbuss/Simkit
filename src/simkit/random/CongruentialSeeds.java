package simkit.random;

/**
* Contains 10 seeds for use with the RandomNumber Congruential
* that give non-overlapping streams. These are the 10 standard
* streams from SIMSCRIPT.
**/
public interface CongruentialSeeds {

    public static final long[] SEED =
       new long[] {
           2116429302L,
           683743814L,
           964393174L,
           1217426631L,
           618433579L,
           1157240309L,
           15726055L,
           48108509L,
           1797920909L,
           477424540L
    };
}
