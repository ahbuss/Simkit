@echo off
if not exist simsystem mkdir simsystem
javac -O -verbose -d simsystem graph\*.java simkit\*.java simkit\data\*.java simkit\random\*.java simkit\smd\*.java simkit\util\*.java simkit\examples\*.java
copy simkit\*.txt simsystem\simkit
cd simsystem
jar cvfM simsystem.zip graph simkit
cd ..
java -cp simsystem\simsystem.zip simkit.Version
pause
