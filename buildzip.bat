@echo off
if not exist simsystem mkdir simsystem
javac -O -verbose -d simsystem graph\*.java simkit\*.java simkit\random\*.java simkit\stat\*.java simkit\smd\*.java simkit\util\*.java simkit\examples\*.java simkit\smdx\*.java
copy simkit\*.txt simsystem\simkit
copy simkit\*.png simsystem\simkit
cd simsystem
jar cvf simkit.jar graph simkit
cd ..