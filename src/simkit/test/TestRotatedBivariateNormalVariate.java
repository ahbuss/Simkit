package simkit.test;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import simkit.random.RandomVector;
import simkit.random.RandomVectorFactory;
import simkit.stat.SimpleStatsTally;

/**
 *
 * @author ahbuss
 */
public class TestRotatedBivariateNormalVariate {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        RandomVector rv = RandomVectorFactory.getInstance("RotatedBivariateNormal");
        System.out.println(rv);
        rv = RandomVectorFactory.getInstance("RotatedBivariateNormal", Math.PI * 0.5);
        System.out.println(rv);
        rv = RandomVectorFactory.getInstance("RotatedBivariateNormal", 3.4, 5.6);
        System.out.println(rv);
        rv = RandomVectorFactory.getInstance("RotatedBivariateNormal", 3.4, 5.6, Math.PI * 0.5);
        System.out.println(rv);
//        rv = RandomVectorFactory.getInstance("RotatedBivariateNormal", -3.4, 5.6); 
//        System.out.println(rv);
        rv = RandomVectorFactory.getInstance("RotatedBivariateNormal", 3.4, 5.6, 7.8, 9.1, Math.PI * 0.5);
        System.out.println(rv);
        rv.setParameters(10.0, 50.0, 1.0, 4.0, -Math.PI * 0.0);
        System.out.println(rv);
        
        Point2D vector = new Point2D.Double(20.0, 10.0);
        double angle = -Math.atan2(vector.getX(), vector.getY());
        
        rv.setParameters(0.0, 0.0, 1.0, 4.0,  angle );
        System.out.println(rv);
        
        SimpleStatsTally[] sst = new SimpleStatsTally[] {
            new SimpleStatsTally("x"),
            new SimpleStatsTally("y")
        };
        
        File outputFile = new File(System.getProperty("user.home"), "output.csv");
        FileWriter fileWriter = new FileWriter(outputFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.append("x,y");
        int number = 100000;
        for (int i = 0; i < number; ++i) {
            double[] gen = rv.generate();
            bufferedWriter.newLine();
            bufferedWriter.append(String.format("%f,%f", gen[0], gen[1]));
            sst[0].newObservation(gen[0]);
            sst[1].newObservation(gen[1]);
        }
        bufferedWriter.flush();
        fileWriter.close();
        for (int i = 0; i< sst.length; ++i) {
            System.out.println(sst[i]);
        }
    }

}
