@echo off
if not exist simsystem mkdir simsystem
javac -O -verbose -d simsystem graph\*.java simkit\*.java simkit\data\*.java simkit\random\*.java simkit\stat\*.java simkit\smd\*.java simkit\util\*.java simkit\examples\*.java simkit\smdx\*.java
copy simkit\*.txt simsystem\simkit
cd simsystem
jar cvf simsystem.zip graph simkit
if not exist doc mkdir doc
cd ..
javadoc -author -private -d simsystem\doc simkit simkit.data simkit.smd simkit.stat simkit.random simkit.util simkit.examples simkit.smdx
java -cp simsystem\simsystem.zip simkit.Version
pause
