package simkit.test;
import simkit.*;
/**
 * Tests new ability to check for minimum version.
 * @version $Id: TestVersion.java 757 2005-04-20 23:18:17Z ahbuss $
 * @author  ahbuss
 */
public class TestVersion {
    
    public static void main(String[] args) {
        System.out.println(Version.getVersion());

        String[] versions = new String[] {
            "1.2.14",
            "0.3.15",
            "1.2.15",
            "1",
            "2",
            "1.1",
            "1.3"
        };
        for (int i = 0; i < versions.length; ++i) {
            System.out.println(versions[i] + " " + Version.isAtLeastVersion(versions[i]));
        }

    }
    
}