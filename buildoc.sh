#!/bin/sh
if [ -d simsystem ]; then
	echo "directory simsystem exists"
else
	if [ -e simsystem]; then 
		rm simsystem
	fi
	echo "directory simsystem doesn't exist"
	mkdir simsystem
fi
if [ -d simsystem/doc ]; then
	rm -rf simsystem/doc/*
else
	mkdir simsystem/doc
fi
javadoc -author -private -d simsystem/doc simkit simkit.data simkit.random simkit.smd simkit.stat simkit.util simkit.examples

