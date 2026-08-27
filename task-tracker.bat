@echo off
mkdir out 2>nul
javac -d out src\*.java
java -cp out CLI %*
