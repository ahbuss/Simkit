#!/bin/bash

# first source the SOURCE_ME environment before running this
# also, you may need to edit the SOURCE_ME to fit your jaxb install
# directory and jdk home

xjc.sh -p simkit.xsd.bindings -extension simkit.xsd -d bindings -b simkit.xjb
