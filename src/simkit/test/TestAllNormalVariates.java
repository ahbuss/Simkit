package simkit.test;

import java.util.ArrayList;
import java.util.List;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.SimpleStatsTally;

/**
 *
 * @author ahbuss
 */
public class TestAllNormalVariates {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         Object[] params = {0.0, 1.0};
         
         String[] names = {
             "Normal", "Normal02", "Normal03", "Normal04"
         };
         
         List<RandomVariate> randomVariates = new ArrayList<>();
         for (String name: names) {
             randomVariates.add(RandomVariateFactory.getInstance(name, params));
         }
         
         
         SimpleStatsTally[] stats = new SimpleStatsTally[randomVariates.size()];
         for (int i = 0; i < stats.length; ++i) {
             stats[i] = new SimpleStatsTally(randomVariates.get(i).getClass().getSimpleName().replace("Variate", ""));
         }
         
         int numberReplications = 10000000;
         
         for (int replication = 1; replication <= numberReplications; ++replication) {
             for (int i = 0; i < randomVariates.size(); ++i) {
                 stats[i].newObservation(randomVariates.get(i).generate());
             }
         }
         
         for (SimpleStatsTally stat: stats) {
             System.out.println(stat);
         }
    }

}
