@echo off
if not exist simsystem mkdir simsystem
if not exist simsystem\doc mkdir simsystem\doc

javadoc -author -private -d simsystem\doc simkit simkit.stat simkit.random simkit.util simkit.examples simkit.smdx

