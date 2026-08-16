@echo off
cd /d "%~dp0"
set "JAVA_HOME=C:\Users\bario\.jdks\openjdk-25"
set "PATH=%JAVA_HOME%\bin;%PATH%"
call mvnw.cmd spring-boot:run
pause
