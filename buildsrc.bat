@echo off
if not exist simsystem mkdir simsystem
jar cvfM simsystem\simkit-src.zip graph\*.java simkit\*.java simkit\*.txt simkit\examples\*.java simkit\random\*.java simkit\smdx\*.java simkit\stat\*.java simkit\test\*.java simkit\util\*.java simkit\xml\*.java *.bat *.sh *.txt
