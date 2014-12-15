package simkit.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
* Used to hold Properties that can be accessed using a block name and 
* property name. Supports reading and writing the properties to 
* a text file. Access to the properties is via the <CODE>get</CODE>
* and <CODE>put</CODE> methods inherited from <CODE>Hashtable2</CODE>.
* <BR>The format of the file is as follows:<BR><BR>
* <CODE>[Block name]<BR>
* Property Name=value<BR>
* .<BR>
* [Block name]<BR>
* #Comment<BR>
* ;Comment (Comments are ignored on load and not stored.)<BR>
* etc.</CODE><P>
* Note: This class is similar to java.util.Properties, but adds
* blocks so that the file format mimics Windows ini file format.
* @see Properties
* @version $Id$
**/
public class INIFileProperties extends LinkedHashMap<String, Map<String, String>> {

/**
* Holds the new line character for the current operating system.
**/
    private static final String NL = System.getProperty("line.separator");

    private static final Logger log = Logger.getLogger("simkit.util");
    
/**
* Creates an empty properties table.
**/
    public INIFileProperties() {
        super();
    }

/**
* Creates a properties table loading the values from the given InputStream.
*  @throws IOException If the file cannot be read.
**/
    public INIFileProperties(InputStream instream) throws IOException  {
        super();
        this.load(instream);
    }

/**
* Creates a properties table loading the values from the given URL.
*  @throws IOException If the file cannot be read.
**/
    public INIFileProperties(URL url) throws IOException {
        super();
        this.load(url);
    }

/**
* Creates a properties table loading the values from the given File.
*  @throws IOException If the file cannot be read.
**/
    public INIFileProperties(File file) throws IOException {
        super();
        this.load(file);
    }

/**
* Creates a properties table loading the values from the file specified.
* @param fileName The path to the file.
**/
    public INIFileProperties(String fileName) {
        super();
        this.load(fileName);
    }
/**
 *  Loads an ini file from the given InputStream. 
 *  @param inStream The InputStream from which to load the ini file.
 *  @throws IOException If the file cannot be read.
*   @throws IllegalINIFormatException If the file contains format errors.
**/
    public void load(InputStream inStream)
            throws IOException {

        int lineNumber = 0;
        BufferedReader input = new BufferedReader(new InputStreamReader(inStream));

        StringTokenizer tokens = null;
        Map<String, String> currentBlock = new LinkedHashMap<String, String>();
        String currentBlockName = "";
        for (String nextLine = input.readLine(); nextLine != null; nextLine = input.readLine()) {
            lineNumber++;
            if (nextLine.startsWith(";") || nextLine.startsWith("#") ){
                continue;
            }
            else if (nextLine.startsWith("[") && nextLine.endsWith("]")) {
                tokens = new StringTokenizer(nextLine, "[]");
                if (tokens.countTokens() == 1) {
                    currentBlockName = tokens.nextToken();
                    currentBlock = new LinkedHashMap<String, String>();
                    this.put(currentBlockName, currentBlock);
                }
                else {
                    String message = " on line " + lineNumber +":" + NL +
                        nextLine + "[# tokens = " + tokens.countTokens() + "]";
                    log.severe(message);
                    throw new IllegalINIFormatException(message);
                }
            }
            else {
                tokens = new StringTokenizer(nextLine, "=");
                switch (tokens.countTokens()) {
                    case 0:
                            break;
                    case 1:
                        currentBlock.put(tokens.nextToken().trim(), "");
                        break;
                    case 2:
                        currentBlock.put(tokens.nextToken().trim(), tokens.nextToken().trim());
                        break;
                    default:
                        String message = "Improper format in ini file at line " + lineNumber +":" + NL +
                                nextLine + "[# tokens = " + tokens.countTokens() + "]";
                        log.severe(message);
                        throw new IllegalINIFormatException(message);
                }
            }
        }
        input.close();
    }

/**
 *  Loads an ini file from the given filename. Note: This version
 *  of load does not throw on IOException, it simply prints a warning 
 *  to stderr.
 *  @param iniFileName the name of the ini file to be loaded.
*   @throws RuntimeException If the file contains format errors.
**/
    public void load(String iniFileName) {
        try {
            this.load(new File(iniFileName));
        }
        catch (IOException e) {
            log.severe(e.toString());
            throw new RuntimeException(e);
        }

    }

/**
 *  Loads an ini file from the given URL. 
 *  @param file The URL from which to load the ini file.
 *  @throws IOException If the file cannot be read.
*   @throws RuntimeException If the file contains format errors.
**/
    public void load(URL file)
            throws IOException {
        InputStream instream = new FileInputStream(file.getFile());
        this.load(instream);
    }

/**
 *  Loads an ini file from the given File.  Simply invokes the Load(URL) method.
 *  @param file The File object from which to load the ini file.
 *  @throws IOException If the file cannot be read.
*   @throws RuntimeException If the file contains format errors.
**/
    public void load(File file)
            throws IOException {
        this.load(file.toURI().toURL());
    }

/**
 *  Saves the Hashtable2 as an ini file to File outFile.  Note that all comments
 *  are lost.
 *  @param outFile The File object to write the ini file to.
 *  @throws IOException If the file cannot be written.
**/
    public void save(File outFile) throws IOException {
        PrintWriter outWriter = new PrintWriter(new FileOutputStream(outFile));
        outWriter.print(this.toString());
        outWriter.flush();
        outWriter.close();
    }

/**
 *  Creates a String containing the properties in output file format.
**/
    public String toString() {
        StringBuilder buf = new StringBuilder();
        boolean first = true;
        for (String blockName : this.keySet() ) {
            if (!first) { buf.append(NL); }
            else { first = false; }
            buf.append('[');
            buf.append(blockName);
            buf.append(']');
            buf.append(NL);
            Map<String, String> blockProperties = this.get(blockName);
            for (String keyName : blockProperties.keySet()) {
                String value = blockProperties.get(keyName);
                buf.append(keyName);
                buf.append(" = ");
                buf.append(value);
                buf.append(NL);
            }
        }
        return buf.toString();
    }

} 
