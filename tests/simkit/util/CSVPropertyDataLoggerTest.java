/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simkit.util;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 *
 * @author ahbuss
 */
public class CSVPropertyDataLoggerTest extends TestCase {

    private static final Logger LOGGER = Logger.getLogger(CSVPropertyDataLoggerTest.class.getName());

    private final File outputFile;
    private final CSVPropertyDataLogger instance;

    public CSVPropertyDataLoggerTest(String testName) {
        super(testName);
        this.outputFile = new File("tests/output.csv");
        this.instance = new CSVPropertyDataLogger(outputFile);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        instance.reset();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if (this.outputFile.exists()) {
            this.outputFile.delete();
        }
    }

    /**
     * Test of reset method, of class CSVPropertyDataLogger.
     */
    public void testReset() {
        System.out.println("reset");
        instance.propertyChange(new PropertyChangeEvent(this, "foo", null, 3.5));
        assertFalse(instance.temporaryDataFiles.isEmpty());
        assertFalse(instance.temporaryDataWriters.isEmpty());
        instance.reset();
        assertTrue(instance.temporaryDataFiles.isEmpty());
        assertTrue(instance.temporaryDataWriters.isEmpty());
    }

    /**
     * Test of propertyChange method, of class CSVPropertyDataLogger.
     */
    public void testPropertyChange() {
        System.out.println("propertyChange");
        String propertyName = "foo";
        int newValue = 100;
        int upperLimit = 110;
        for (int i = newValue; i < upperLimit; ++i) {
            PropertyChangeEvent evt = new PropertyChangeEvent(this, propertyName, null, i);
            instance.propertyChange(evt);
            assertEquals(1, instance.temporaryDataFiles.size());
            assertEquals(propertyName, instance.temporaryDataFiles.keySet().toArray()[0]);
        }

        assertEquals(1, instance.temporaryDataWriters.keySet().size());
        assertEquals(propertyName, instance.temporaryDataWriters.keySet().toArray()[0]);

        File tempFile = instance.temporaryDataFiles.get(propertyName);
        assertNotNull(tempFile);
        String tempFileName = tempFile.getName();
        assertTrue(tempFileName.endsWith(".csv"));
        assertTrue(tempFileName.startsWith(propertyName));

        try {
            instance.temporaryDataWriters.get(propertyName).flush();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        try {
            Scanner scanner = new Scanner(tempFile);
            int expected = newValue;
            while (scanner.hasNext()) {
                int result = scanner.nextInt();
                assertEquals(expected, result);
                expected += 1;
            }
            scanner.close();

        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of writeOutputFile method, of class CSVPropertyDataLogger.
     */
    public void testWriteOutputFile() {
        System.out.println("writeOutputFile");
        String propertyName = "bar";
        int newValue = 100;
        int upperLimit = 110;
        for (int i = newValue; i < upperLimit; ++i) {
            PropertyChangeEvent evt = new PropertyChangeEvent(this, propertyName, null, i);
            instance.propertyChange(evt);
            assertEquals(1, instance.temporaryDataFiles.size());
            assertEquals(propertyName, instance.temporaryDataFiles.keySet().toArray()[0]);
        }
        instance.writeOutputFile();

        try {
            Scanner scanner = new Scanner(outputFile);
            int expected = newValue;
            String name = scanner.next();
            assertEquals(propertyName, name);
            while (scanner.hasNext()) {
                int result = scanner.nextInt();
                assertEquals(expected, result);
                expected += 1;
            }
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

}
