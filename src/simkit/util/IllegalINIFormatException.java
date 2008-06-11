package simkit.util;

/**
* Used to indicate that a INI file contained a formatting error on read.
* @version $Id: IllegalINIFormatException.java 476 2003-12-09 00:27:33Z jlruck $
**/
public class IllegalINIFormatException extends RuntimeException {

/**
* Creates a new exception with no description.
**/
  public IllegalINIFormatException() {
    super();
  }

/**
* Creates an exception with the given description.
**/
  public IllegalINIFormatException(String s) {
    super(s);
  }
} 
