@echo off
call buildzip
call buildsrc
call buildoc

java -cp simsystem\simkit.jar simkit.Version
pause
