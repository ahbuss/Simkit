@rem Create private and public javadoc for simkit and graph. Errors and Warnings go to stdout.
@rem John Ruck, Rolands and Associates Corporation
@rem 9/17/03
javadoc -public -d c:\simkit_javadoc\public -sourcepath c:\tmp\simkit -subpackages simkit:graph -classpath c:\tmp\simkit\lib\jdom.jar -breakiterator > c:\simkit_javadoc\output.txt
javadoc -private -d c:\simkit_javadoc\private -sourcepath c:\tmp\simkit -subpackages simkit:graph -classpath c:\tmp\simkit\lib\jdom.jar -breakiterator > c:\simkit_javadoc\output.txt

