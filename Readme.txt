Simkit Copyright (C) 1997-2025 Arnold Buss and Kirk Stork

Installation Instructions
------------ ------------
First install the Java Developer Kit version 1.8 (J2SE8) or greater.  
Unzip this file and add simkit.jar to your classpath. If you wish to use
Classfinder, copy the config directory and its contents to the root directory of
your project

Any questions regarding Simkit may be addressed by e-mail to
abuss@nps.edu

Compilation Instructions
----------- ------------

If you have the source files, there should be an Ant build file called build.xml.  
Executing the dist target ("ant dist" on the command line) will create all 
the necessary files in a "dist" directory.  The important file is simkit.jar, 
that should be put on the classpath of any Java application that uses it.
The source files should be supplied with the distribution
in a file called simkit-src.jar, and can be unpacked with the jar tool or 
with any zip application (e.g. 7-zip, http://www.7-zip.org/).

The key Ant targets are as follows:

clean - deletes build and dist directories, and their contents
compile - compiles the classes to the build directory
jar - creates simkit.jar in the build directory
doc - generates the javadoc in build/doc
jar.source - creates simkit-src.jar file of all Simkit's source files
dist - puts all of the files in a dist directory
