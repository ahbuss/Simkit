if not exist simsystem mkdir simsystem
javac -verbose -d simsystem graph\*.java simkit\*.java simkit\data\*.java simkit\random\*.java simkit\smd\*.java simkit\util\*.java
copy simkit\*.txt simsystem\simkit
cd simsystem
jar cvfM simsystem.zip graph simkit
cd ..
pause
