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
javac -O -classpath . -verbose -d simsystem graph/*.java simkit/*.java simkit/data/*.java simkit/random/*.java simkit/smd/*.java simkit/util/*.java simkit/examples/*.java
cp simkit/*.txt simsystem/simkit
cd simsystem
jar cvfM simsystem.zip graph simkit
cd ..
java -cp simsystem/simsystem.zip simkit.Version


