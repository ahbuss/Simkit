@echo off
if not exist build mkdir build
javac -classpath . -O -verbose -d build graph\*.java simkit\*.java simkit\random\*.java simkit\stat\*.java simkit\smd\*.java simkit\util\*.java simkit\examples\*.java simkit\smdx\*.java simkit\xml\*.java
copy simkit\*.txt build\simkit
copy simkit\*.png build\simkit
copy simkit\xml\*.xml build\simkit\xml
copy simkit\xml\*.dtd build\simkit\xml
cd build
jar cvf simkit.jar graph simkit
cd ..