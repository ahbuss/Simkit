@echo off
if not exist simsystem mkdir simsystem
javac -O -classpath .;lib\jdom.jar -verbose -d simsystem graph\*.java simkit\*.java simkit\data\*.java simkit\random\*.java simkit\stat\*.java simkit\smd\*.java simkit\util\*.java simkit\examples\*.java simkit\smdx\*.java simkit\xml\*.java
copy simkit\*.txt simsystem\simkit
copy simkit\*.png simsystem\simkit
cd simsystem
jar cvf simkit.jar graph simkit
if not exist doc mkdir doc
cd ..
javadoc -author -private -d simsystem\doc simkit simkit.data simkit.smd simkit.stat simkit.random simkit.util simkit.examples simkit.smdx simkit.xml
jar cvf simsystem\simkit-src.zip simkit
jar cvf simsystem\simkit-doc.zip -C simsystem doc
java -cp simsystem\simkit.jar simkit.Version
pause
