@echo off
rem Usage: run.bat [server-url]   (needs JDK 11+)
if not exist out mkdir out
javac -d out src\com\racer\game\*.java && java -cp out com.racer.game.GameFrame %*
