@echo off
if not exist build mkdir build
if not exist build\doc mkdir build\doc

javadoc -author -private -d build\doc simkit simkit.stat simkit.random simkit.util simkit.examples simkit.smdx simkit.xml

