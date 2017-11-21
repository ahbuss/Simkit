package simkit.test;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import simkit.SimEntity;
import simkit.SimEntityBase;
import simkit.SimEntityFactory;
import simkit.examples.ArrivalProcess;
import simkit.random.RandomVariateFactory;
import simkit.smd.BasicLinearMover;

/**
 *
 * @author ahbuss
 */
public class TestSimEntityFactory {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SimEntity simEntity = new ArrivalProcess(
                RandomVariateFactory.getInstance("Exponential", 1.9));

        System.out.print("parameters: ");

        System.out.println("Live: " + simEntity);
        SimEntity copy = SimEntityFactory.createCopy(simEntity);
        System.out.println("Memorex: " + copy);
        System.out.println(SimEntityFactory.getParameters(simEntity));
        System.out.println(SimEntityFactory.getParameters(copy));
        List<Integer> list = new LinkedList<>();
        list.add(42);
        list.add(73);
        simEntity = new TestSimEntity(5, 3.14159, "Buckle",
                new double[]{1.2, 3.4, 5.6}, list
        );
        System.out.print("parameters: ");
        System.out.println(SimEntityFactory.getParameters(simEntity));
        copy = SimEntityFactory.createCopy(simEntity);
        System.out.println("Live: " + simEntity);
        System.out.println("Memorex: " + copy);

        List<SimEntity> copies = SimEntityFactory.createCopies(simEntity, 5);
        System.out.printf("%d copies:%n", copies.size());
        for (SimEntity se : copies) {
            System.out.println(se);
        }
        
//        Now let's try on Movers...
        simEntity = new BasicLinearMover("Original", new Point2D.Double(10.0, 20.0), 30.0);
        List<SimEntity> movers = SimEntityFactory.createCopies(simEntity, 5);
        System.out.printf("%d movers created:%n", movers.size());
        for (SimEntity mover: movers) {
//            mover.reset();
            System.out.println(mover);
        }
        
    }

    public static class TestSimEntity extends SimEntityBase {

        private int one;

        private double two;

        private String buckle;

        private double[] my;

        private List<Integer> shoe;

        public TestSimEntity() {
        }

        public TestSimEntity(int one, double two, String buckle,
                double[] my, List<Integer> shoe) {
            this.setOne(one);
            this.setTwo(two);
            this.setBuckle(buckle);
            this.setMy(my);
            this.setShoe(shoe);
        }

        /**
         * @return the one
         */
        public int getOne() {
            return one;
        }

        /**
         * @param one the one to set
         */
        public void setOne(int one) {
            this.one = one;
        }

        /**
         * @return the two
         */
        public double getTwo() {
            return two;
        }

        /**
         * @param two the two to set
         */
        public void setTwo(double two) {
            this.two = two;
        }

        /**
         * @return the buckle
         */
        public String getBuckle() {
            return buckle;
        }

        /**
         * @param buckle the buckle to set
         */
        public void setBuckle(String buckle) {
            this.buckle = buckle;
        }

        /**
         * @return the my
         */
        public double[] getMy() {
            return my.clone();
        }

        /**
         * @param my the my to set
         */
        public void setMy(double[] my) {
            this.my = my.clone();
        }

        /**
         * @return the shoe
         */
        public List<Integer> getShoe() {
            return new ArrayList<>(shoe);
        }

        /**
         * @param shoe the shoe to set
         */
        public void setShoe(List<Integer> shoe) {
            this.shoe = new ArrayList<>(shoe);
        }

    }

}
