@rem Create private and public javadoc for simkit. Errors and Warnings go to stdout.
@rem John Ruck, Rolands and Associates Corporation
@rem 9/17/03
@rem The Header: $Header$
@rem The log: $Log$
@rem The log: Revision 1.4  2003/10/27 17:16:36  jlruck
@rem The log: Removed graph, added version and Author flags
@rem The log:
javadoc -public -d c:\simkit_javadoc\public -sourcepath c:\tmp\simkit -subpackages simkit -classpath c:\tmp\simkit\lib\jdom.jar -breakiterator -link http://java.sun.com/j2se/1.4/docs/api/ > c:\simkit_javadoc\output.txt
javadoc -private -version -author -d c:\simkit_javadoc\private -sourcepath c:\tmp\simkit -subpackages simkit -classpath c:\tmp\simkit\lib\jdom.jar -breakiterator -link http://java.sun.com/j2se/1.4/docs/api/ > c:\simkit_javadoc\output.txt

