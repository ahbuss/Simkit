package simkit;

import java.io.*;
import java.util.*;
import simkit.util.INIFileProperties;

public class Version {

   private static final Version VERSION = new Version();

   private static String SIMKIT_VERSION = "Not Found";
   private static Properties SIMKIT_PROPERTIES;
   private static String SIMKIT_COPYRIGHT = "";
   private static String SIMKIT_MESSAGE = "";

      static {
          InputStream is = null;
          try {
              BufferedReader br = new BufferedReader(new InputStreamReader(
                  Version.class.getResourceAsStream("version.txt")));
              StringBuffer buf = new StringBuffer();
              for (String nextLine = br.readLine(); nextLine != null; nextLine = br.readLine()) {
                  buf.append(nextLine);
                  buf.append(SimEntity.NL);
              }
              SIMKIT_VERSION = buf.toString();
              br.close();

              br = new BufferedReader(new InputStreamReader(
                  Version.class.getResourceAsStream("copyright.txt")));
              buf = new StringBuffer();
              for (String nextLine = br.readLine(); nextLine != null; nextLine = br.readLine()) {
                  buf.append(nextLine);
                  buf.append(SimEntity.NL);
              }
              SIMKIT_COPYRIGHT = buf.toString();
              br.close();

              br = new BufferedReader(new InputStreamReader(
                  Version.class.getResourceAsStream("gnu.txt") ) );
              buf = new StringBuffer();
              for (String nextLine = br.readLine(); nextLine != null; nextLine = br.readLine()) {
                  buf.append(nextLine);
                  buf.append(SimEntity.NL);
              }
              SIMKIT_MESSAGE = buf.toString();
              br.close();
          } catch (IOException e) {}
      }

   private Version() { }

   public static final String getCopyright() {
     return VERSION.SIMKIT_COPYRIGHT;
   }

   public static final String getMessage() {
     return VERSION.SIMKIT_MESSAGE;
   }

   public static final String getVersion() {return VERSION.SIMKIT_VERSION;}

   public static void main(String[] args) {
     System.out.println("Simkit version " + getVersion() + SimEntity.NL +
       getCopyright() + SimEntity.NL + getMessage());
   }
} 
