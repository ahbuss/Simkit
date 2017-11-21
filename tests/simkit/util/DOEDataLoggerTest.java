/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simkit.util;

import java.beans.PropertyChangeEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 * @author ahbuss
 */
public class DOEDataLoggerTest extends TestCase {

    private DOEDataLogger doeDataLogger;

    private String propertyName = "propertyName";

    private String[] header = new String[]{"factor1", "factor2"};

    private String outputFileName = "testOutput.csv";
    
    private Object[] factors = {3, 4.5};

    public DOEDataLoggerTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        doeDataLogger = new DOEDataLogger(propertyName, outputFileName, header, factors);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        File outputFile = new File(outputFileName);
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }

    /**
     * Test of propertyChange method, of class DOEDataLogger.
     */
    public void testPropertyChange() {
        System.out.println("propertyChange");
        double newValue = Math.PI;
        PropertyChangeEvent evt = new PropertyChangeEvent(this, propertyName, null, newValue);
        doeDataLogger.propertyChange(evt);
        doeDataLogger.close();
        try {
            FileReader reader = new FileReader(new File(outputFileName));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String readHeader = bufferedReader.readLine();
            String dataLine = bufferedReader.readLine();
            bufferedReader.close();
            String expected = doeDataLogger.getFactorString() + newValue;
            assertEquals(expected, dataLine);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DOEDataLoggerTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DOEDataLoggerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of writeHeader method, of class DOEDataLogger.
     */
//    public void testWriteHeader() {
//        FileReader fileReader = null;
//        try {
//            System.out.println("buildFactorString");
//            doeDataLogger.writeHeader();
//            doeDataLogger.close();
//            File outputTestFile = new File(outputFileName);
//            fileReader = new FileReader(outputTestFile);
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            String firstRow = bufferedReader.readLine();
//            String expected = header[0] + doeDataLogger.getDelimiter()
//                    + header[1] + doeDataLogger.getDelimiter() + propertyName;
//            assertEquals(expected, firstRow);
//            // TODO review the generated test code and remove the default call to fail.
////            fail("The test case is a prototype.");
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(DOEDataLoggerTest.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(DOEDataLoggerTest.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            try {
//                fileReader.close();
//            } catch (IOException ex) {
//                Logger.getLogger(DOEDataLoggerTest.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }

    /**
     * Test of buildFactorString method, of class DOEDataLogger.
     */
    public void testBuildFactorString() {
        System.out.println("writeHeader");
        doeDataLogger.writeHeader();
        doeDataLogger.buildFactorString();
        String expectedFactorString = "" + factors[0] + doeDataLogger.getDelimiter() +
                factors[1] + doeDataLogger.getDelimiter();
        assertEquals(expectedFactorString, doeDataLogger.getFactorString());
    }

    /**
     * Test of setFactors method, of class DOEDataLogger.
     */
    public void testSetFactors() {
        System.out.println("setFactors");
        Object[] factors = {5, 6};
        doeDataLogger.setFactors(factors);
        for (int i = 0; i < factors.length; ++i) {
            assertEquals(factors[i], doeDataLogger.getFactors()[i]);
        }
    }

    public void testSetDelimiter() {
        System.out.println("setDelimiter");

        try {
            doeDataLogger.setDelimiter("\t");
            fail("setDelimiter should have thrown RuntimeException");
        } catch (RuntimeException e) {
        }
    }

    public void testSetHeader() {
        System.out.println("setHeader");
        try {
            doeDataLogger.setHeader("foo", "bar");
            fail("setHeader should have thrown RuntimeException");
        } catch (RuntimeException e){            
        }
    }
    
    public void testZeroConstructor() {
        System.out.println("DOEDataLogger");
        
        doeDataLogger = new DOEDataLogger();
        try {
            doeDataLogger.setPropertyName(propertyName);
            doeDataLogger.setHeader(header);
            doeDataLogger.setFactors(factors);
            doeDataLogger.setDelimiter(",");
            doeDataLogger.setOutputFile(new File(outputFileName));
            doeDataLogger.buildFactorString();
            doeDataLogger.writeHeader();
            doeDataLogger.close();
        } catch (IOException ex) {
            Logger.getLogger(DOEDataLoggerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (int i = 0; i < header.length; ++i) {
            assertEquals(header[i], doeDataLogger.getHeader()[i]);
            assertEquals(factors[i], doeDataLogger.getFactors()[i]);
        }
    }

}
