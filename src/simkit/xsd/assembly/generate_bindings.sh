#!/bin/bash

# first source the SOURCE_ME environment before running this
# also, you may need to edit the SOURCE_ME to fit your jaxb install
# directory and jdk home

java -jar ../../../lib/trang.jar -I dtd -O xsd assembly.dtd assembly.xsd
xjc.sh -p simkit.xsd.bindings.assembly assembly.xsd -extension -d bindings
mv bindings/simkit/xsd/bindings/assembly ../bindings
javadoc -sourcepath ../../.. -d ../doc simkit.xsd.bindings.assembly simkit.xsd.bindings
