Simkit version 1.2.3
Copyright (C) 1997-2002 Kirk Stork and Arnold Buss

Installation Instructions
------------ ------------
First install the Java Developer Kit version 1.2 (Java 2) or greater.  
For Windows 95/98, make sure your CLASSPATH variable is correctly set in the
AUTOEXEC.BAT file (located in C:\).  For Windows NT 4.0/Windows 2000, you will need to
edit the environment, located in the System icon of the Conrol Panel.


Double-click the file simkit-1.2.3.exe and follow the 
directions.  You may choose to put the simsystem.zip file
anywhere you like.  

Any questions regarding Simkit may be addressed by e-mail to
abuss@nps.navy.mil.

Uninstallation Instructions
-------------- ------------

Open the Control Panel (e.g. click Start | Settings | Control Panel).
Double-click "Add/Remove Programs" icon.  Scroll to "Simkit" and 
click on it.  Click Add/Remove.

When you uninstall Simkit the AUTOEXEC.BAT entry or the environment variables
will not be removed; you should remove them manually.

Compilation Instructions
----------- ------------

If you have the source files, there sould be a file called build.bat (for Windows)
or a file called build.sh (for Unix and Unix-like systems).  Executing this script will
compile and build a simsystem.zip file in a dierctory called simsystem.  After executing
the script, put the simsystem.zip file on your classpath.