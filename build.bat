@echo off
call buildzip
call buildsrc
call buildoc

java -cp build\simkit.jar simkit.Version
pause
