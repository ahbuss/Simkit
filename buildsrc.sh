#!/bin/sh

if [ ! -d simsystem ]; then
	mkdir simsystem
fi
tar cvfz simsystem/simkit-src.tar.gz graph/*.java simkit/*.java simkit/*/*.java *.bat *.sh *.txt *.jpr
