#!/bin/sh
if [ -d simsystem ]; then
	echo "directory simsystem exists"
	rm -rv simsystem/simkit simsystem/graph
else
	if [ -e simsystem]; then 
		rm simsystem
	fi
	echo "directory simsystem doesn't exist"
	mkdir simsystem
fi
javac -O -classpath . -verbose -d simsystem graph/*.java simkit/*.java simkit/data/*.java simkit/random/*.java simkit/stat/*.java simkit/smd/*.java simkit/util/*.java simkit/examples/*.java simkit/smdx/*.java
cp simkit/*.txt simsystem/simkit
cd simsystem
jar cvf simkit.jar graph simkit
if [ ! -d doc ]; then
	mkdir doc
fi
cd ..
javadoc -author -private -d simsystem/doc simkit simkit.data simkit.random simkit.smdx simkit.smd imkit.stat simkit.util simkit.examples
java -cp simsystem/simkit.jar simkit.Version

