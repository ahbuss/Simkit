@echo off
call buildzip
call buildoc
call buildsrc

java -cp simsystem\simkit.jar simkit.Version
pause
