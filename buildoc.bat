@echo off
if not exist simsystem mkdir simsystem
if not exist simsystem\doc mkdir simsystem\doc
cd ..
javadoc -author -private -d simsystem\doc simkit simkit.data simkit.smd simkit.stat simkit.random simkit.util simkit.examples simkit.smdx
pause
