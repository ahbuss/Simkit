package simkit.util;

import java.io.*;
import java.net.*;
import java.util.*;

/**
* Used to hold Properties that can be accessed using a block name and 
* property name. Supports reading and writing the properties to 
* a text file. Access to the properties is via the <CODE>get<CODE/>
* and <CODE>put<CODE/> methods inherited from <CODE>Hashtable2<CODE/>.
* <BR/>The format of the file is as follows:<BR/><BR/>
* <CODE>[Block name]<BR/>
* Property Name=value<BR/>
* .<BR/>
* [Block name]<BR/>
* #Comment<BR/>
* ;Comment (Comments are ignored on load and not stored.)<BR/>
* etc.<CODE/><P/>
* Note: This class is similar to java.util.Properties, but adds
* blocks so that the file format mimics Windows ini file format.
* @see Properties
**/
public class INIFileProperties extends Hashtable2 {

/**
* Holds the new line character for the current operating system.
**/
    private static final String NL = System.getProperty("line.separator");

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
*   @throws RuntimeException If the file contains format errors.
**/
    public void load(InputStream inStream)
            throws IOException {

        int lineNumber = 0;
        BufferedReader input = new BufferedReader(new InputStreamReader(inStream));

        StringTokenizer tokens = null;
        Properties currentBlock = new Properties();
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
                    currentBlock = new Properties();
                    this.put(currentBlockName, currentBlock);
                }
                else {
                    throw new RuntimeException(" on line " + lineNumber +":" + NL +
                        nextLine + "[# tokens = " + tokens.countTokens() + "]");
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
                        throw new RuntimeException (
                            "Improper format in ini file at line " + lineNumber +":" + NL +
                                nextLine + "[# tokens = " + tokens.countTokens() + "]");
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
        catch (IOException e) { System.err.println(e); }

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
        this.load(file.toURL());
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
        StringBuffer buf = new StringBuffer();
        for (Iterator block = this.keySet().iterator(); block.hasNext(); ) {
            Object blockName = block.next();
            buf.append('[');
            buf.append(blockName);
            buf.append(']');
            buf.append(NL);
            Map blockProperties = (Map)this.get(blockName);
            for (Iterator key = blockProperties.keySet().iterator(); key.hasNext();) {
                Object keyName = key.next();
                Object value = blockProperties.get(keyName);
                buf.append(keyName);
                buf.append('=');
                buf.append(value);
                buf.append(NL);
            }
            if (block.hasNext()) {buf.append(NL);}
        }
        return buf.toString();
    }

} 
