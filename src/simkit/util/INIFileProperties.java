package simkit.util;

import java.io.*;
import java.net.*;
import java.util.*;

public class INIFileProperties extends Hashtable2 {

    private static final String NL = System.getProperty("line.separator");

    public INIFileProperties() {
        super();
    }

    public INIFileProperties(InputStream instream) throws IOException  {
        super();
        this.load(instream);
    }

    public INIFileProperties(URL url) throws IOException {
        super();
        this.load(url);
    }

    public INIFileProperties(File file) throws IOException {
        super();
        this.load(file);
    }

    public INIFileProperties(String fileName) {
        super();
        this.load(fileName);
    }
/**
 *  Load an ini file from the given InputStream. 
 *  @param inStream The InputStream from which to load the ini file.
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
 *  Load an ini file from the given filename -- note, does thot throw a checked
 *  exception!
 *  @param iniFileName the name of the ini file to be loaded.
**/
    public void load(String iniFileName) {
        try {
            this.load(new File(iniFileName));
        }
        catch (IOException e) { System.err.println(e); }

    }

/**
 *  Load an ini file from the given URL. 
 *  @param file The URL from which to load the ini file.
**/
    public void load(URL file)
            throws IOException {
        InputStream instream = new FileInputStream(file.getFile());
        this.load(instream);
    }

/**
 *  Load an ini file from the given File.  Simply invokes the Load(URL) method.
 *  @param file The File object from which to load the ini file.
**/
    public void load(File file)
            throws IOException {
        this.load(file.toURL());
    }

/**
 *  Save the Hashtable2 as an ini file to File outFile.  Note that all comments
 *  are lost.
 *  @param outFile The File object to write the ini file to.
**/
    public void save(File outFile) throws IOException {
        PrintWriter outWriter = new PrintWriter(new FileOutputStream(outFile));
        outWriter.print(this.toString());
        outWriter.flush();
        outWriter.close();
    }

/**
 *  Overrides toString() to print out the Hashtable2 as an ini file String.
 *  @return The stringification of the Hashtable2 in an "ini" format.
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