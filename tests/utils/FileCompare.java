
package utils;

import junit.framework.Assert;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import java.util.*;
/**
* Used to compare 2 files to make sure they are identical.
* If they are not, report the first line number that doesn't match.
**/
public class FileCompare extends Assert {

/**
* Compares two files, failing a junit assertEquals if they are different
**/
    public static void assertSameFile(String file1, String file2) {
        ClassLoader cl = FileCompare.class.getClassLoader();
        LineNumberReader reader1 = null;
        LineNumberReader reader2 = null;
        try {
            reader1 = new LineNumberReader(new FileReader(file1));
        } catch (FileNotFoundException e) {
            InputStream in = cl.getResourceAsStream(file1);
            if (in == null) {
                fail(file1 + " was not found as a file or a resource.");
            } else {
                reader1 = new LineNumberReader(new InputStreamReader(in));
            }
        }
        try {
            reader2 = new LineNumberReader(new FileReader(file2));
        } catch (FileNotFoundException e) {
            InputStream in = cl.getResourceAsStream(file2);
            if (in == null) {
                fail(file2 + " was not found as a file or a resource.");
            } else {
                reader2 = new LineNumberReader(new InputStreamReader(in));
            }
        }
        String line1 = null;
        String line2 = null;
        int i = 0;
        while (true) {
            i++;
            try {
                line1 = reader1.readLine();
            } catch (IOException e) {
                fail(e + " while reading a line from " + file1);
            }
            try {
                line2 = reader2.readLine();
            } catch (IOException e) {
                fail(e + " while reading a line from " + file2);
            }
            assertEquals("Line: " + i + " differs between " + file1 + " and " + file2, 
                            line1, line2);
            if (line1 == null) {
                break;
            }
        }
        assertTrue(true);
    }

/**
* Compares two sources of XML. A file is the expected value, and a String is the tested value.
**/
    public static void assertSameXml(String file, String string) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.err.println("SEVERE: ExperimentDriver.assertSameXml(), "
                + "error while getting the XML DocumentBuilder: " + e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        Document fileDoc;
        InputStream in = FileCompare.class.getResourceAsStream(file);
        if (in == null) {
            try {
                in = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                System.err.println("SEVERE: ExperimentDriver.assertSameXml(), "
                    + "The XML file " + file + " was not found as a file or a resource.");
                throw new RuntimeException(e);
            }
        }
        try {
            fileDoc = builder.parse(in);
        } catch (FileNotFoundException e) {
            System.err.println("SEVERE: ExperimentDriver.assertSameXml(), "
                + "The XML file " + file + " was not found. " + e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("SEVERE: ExperimentDriver.assertSameXml(), "
                + "An IOException occured while attempting to open the XML file "
                + file+ " " + e);
            throw new RuntimeException(e);
        } catch (org.xml.sax.SAXException e) {
            System.err.println("SEVERE: ExperimentDriver.assertSameXml(), "
                + e + " while attempting to parse the XML file " + file);
            throw new RuntimeException(e);
        }

        Document stringDoc;
        try {
            stringDoc = builder.parse(new InputSource(new StringReader(string)));
        } catch (IOException e) {
            System.err.println("SEVERE: ExperimentDriver.assertSameXml(), "
                + "An IOException occured while attempting to parse the XML string");
            throw new RuntimeException(e);
        } catch (org.xml.sax.SAXException e) {
            System.err.println("SEVERE: ExperimentDriver.assertSameXml(), "
                + e + " while attempting to parse the XML file " + file);
            throw new RuntimeException(e);
        }
// Since implementations of Document do not override equals, we do the compare manually.
//  Warning: This comparison may not work for all Documents, but it does its best.
//  it will likely report some documents differ that have the equivelent data.

        Element fileRoot = fileDoc.getDocumentElement();
        Element stringRoot = stringDoc.getDocumentElement();
        
        compareElements(fileRoot, stringRoot);
        assertTrue(true);

    }

/**
* Compares the two given elements for equivelence. 
**/
    protected static void compareElements(Element expected, Element actual) {
        if (!(expected.getTagName().equals(actual.getTagName()))) {
            fail("The two Elements do not have the same name " + expected.getTagName() +
                ", " + actual.getTagName());
        }
        NamedNodeMap expectedAttributes = expected.getAttributes();
        NamedNodeMap actualAttributes = actual.getAttributes();
        
    
        if (expectedAttributes.getLength() != actualAttributes.getLength()) {
            fail("The Elements " + expected.getTagName() 
                + " do not have the same number of attributes.");
        }

        for (int i = 0 ; i < expectedAttributes.getLength() ; i++) {
            Attr expectedAttr = (Attr)expectedAttributes.item(i);
            Attr actualAttribute = (Attr)actualAttributes.getNamedItem(expectedAttr.getName());
            if (actualAttribute == null) {
                fail("Both elements do not have an attribute named " + expectedAttr.getName());
            }
            compareAttributes(expectedAttr, actualAttribute);
        }
        NodeList expectedElements = expected.getElementsByTagName("*");
        NodeList actualElements = actual.getElementsByTagName("*");

        if (expectedElements.getLength() != actualElements.getLength()) {
            fail("The elements " + expected.getTagName() + " do not have the same "
                + "number of child elements.");
        }
        Map<String, Element> expectedMap = new HashMap<String, Element>();
        Map<String, Element> actualMap = new HashMap<String, Element>();
        for (int i = 0 ; i < expectedElements.getLength(); i++) {
            Element expectedElement = (Element)expectedElements.item(i);
            Element actualElement = (Element)actualElements.item(i);
            expectedMap.put(expectedElement.getTagName(), expectedElement);
            actualMap.put(actualElement.getTagName(), actualElement);
        }
        Set expectedSet = expectedMap.keySet();
        Set actualSet = expectedMap.keySet();
        if (!(expectedSet.equals(actualSet))) {
            fail("The child elements of " + expected.getTagName() + " do not all have "
                + "the same name");
        }
        Iterator itt = expectedSet.iterator();
        while (itt.hasNext()) {
            String expectedName = (String)itt.next();
            Element expectedElement = (Element)expectedMap.get(expectedName);
            Element actualElement = (Element)actualMap.get(expectedName);
            compareElements(expectedElement, actualElement);
        }
    }

/**
* Compares two Attributes to see if they are equivelent.
* There may be other cases where two attributes should be concidered equal, that are not
* covered that will need to be added when they show up.
**/
    protected static void compareAttributes(Attr expected, Attr actual) {
        if (!(expected.getName().equals(actual.getName()))) {
            fail("The two attributes do not have the same name " + expected.getName()
                + ", " + actual.getName());
        }
        String expectedString = expected.getValue();
        String actualString = actual.getValue();
        if (expectedString.equals(actualString)) {
            return;
        }
//Equivelent numbers
        try {
            double expectedDouble = Double.parseDouble(expectedString);
            double actualDouble = Double.parseDouble(actualString);
            if (expectedDouble == actualDouble) {
                return;
            }
        } catch (NumberFormatException e) {
        }
        fail("The Attributes " + expected.getName() + " are not equal.");
    }
}
        
