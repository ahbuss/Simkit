@echo off
if not exist simsystem mkdir simsystem
jar cvfM simsystem\simkit-src.zip graph\*.java simkit\*.java simkit\*.txt simkit\random\*.java simkit\stat\*.java simkit\smdx\*.java simkit\examples\*.java *.bat *.sh *.txt
