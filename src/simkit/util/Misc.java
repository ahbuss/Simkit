package simkit.util;

import java.lang.reflect.*;

public class Misc {
/**
 *  Given an <CODE>Object[]</CODE> array, return the corresponding <CODE>Class[]</CODE>
 *  array.
 *  @since Simkit 1.0
 *  @param args The <CODE>Object[]</CODE> array we want the signature for.
 *  @return The <CODE>Class[]</CODE> representing the signature of the argument array. 
**/
  public static Class[] getSignatureFromArguments(Object[] args) {
      Class[] signature = new Class[args.length];
      for (int i = 0; i < signature.length; i++) {
        signature[i] = args[i].getClass();
      }
      return signature;
  }

  public String getFullMethodName(String methodName, Object[] arguments) {
    return "";
  }
}
