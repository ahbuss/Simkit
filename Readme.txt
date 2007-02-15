Simkit Copyright (C) 1997-2007 Kirk Stork and Arnold Buss

Installation Instructions
------------ ------------
First install the Java Developer Kit version 1.5 (Java 2) or greater.  
For Windows 2000/Windows XP, you will need to
edit the environment, located in the System icon of the Conrol Panel.

Any questions regarding Simkit may be addressed by e-mail to
abuss@nps.navy.mil.

Uninstallation Instructions
-------------- ------------

Open the Control Panel (e.g. click Start | Settings | Control Panel).
Double-click "Add/Remove Programs" icon.  Scroll to "Simkit" and 
click on it.  Click Add/Remove and follow the dialogs.

When you uninstall Simkit the environment variables
will not be removed; you should remove them manually.

Compilation Instructions
----------- ------------

If you have the source files, there sould be an Ant build file called build.xml.  
Executing the dist target ("ant dist" on the command line) will create all 
the necessary files in a "dist" directory.  The important file is simkit.jar, 
that should be put on the classpath of any Java application that uses it.
The source files should be supplied with the distribution
in a file called simkit-src.jar, and can be unpacked with the jar tool or 
with any zip application (e.g. WinZip).

The key Ant targets are as follows:

clean - deletes build and dist directories, and their contents
compile - compiles the classes to the build directory
jar - creates simkit.jar in the build directory
doc - generates the javadoc in build/doc
jar.source - creates simkit-src.jar file of all Simkit's source files
dist - puts all of the files in a dist directory
