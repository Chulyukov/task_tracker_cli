@echo off
setlocal
chcp 65001 >nul
mkdir out 2>nul

javac -encoding UTF-8 -d out src\*.java
if errorlevel 1 exit /b %errorlevel%

java -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 -cp out CLI %*
if errorlevel 1 exit /b %errorlevel%
