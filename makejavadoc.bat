@rem Create private and public javadoc for simkit. Errors and Warnings go to stdout.
@rem John Ruck, Rolands and Associates Corporation
@rem 9/17/03
@rem $Id$
javadoc -public -d c:\simkit_javadoc\public -sourcepath c:\tmp\simkit -subpackages simkit -classpath c:\tmp\simkit\lib\jdom.jar -breakiterator -link http://java.sun.com/j2se/1.4/docs/api/ > c:\simkit_javadoc\output.txt
javadoc -private -version -author -d c:\simkit_javadoc\private -sourcepath c:\tmp\simkit -subpackages simkit -classpath c:\tmp\simkit\lib\jdom.jar -breakiterator -link http://java.sun.com/j2se/1.4/docs/api/ > c:\simkit_javadoc\output.txt

