#!/bin/sh

if [ ! -d simsystem ]; then
	mkdir simsystem
fi
tar cvfz simsystem/simkit-src.tar.gz graph simkit *.bat *.sh *.txt
