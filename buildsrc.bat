@echo off
if not exist build mkdir build
jar cvfM build\simkit-src.zip graph\*.java simkit\*.java simkit\*.txt simkit\examples\*.java simkit\random\*.java simkit\smdx\*.java simkit\stat\*.java simkit\test\*.java simkit\util\*.java simkit\xml\*.java simkit\xml\*.dtd simkit\xml\*.xml *.bat *.txt
