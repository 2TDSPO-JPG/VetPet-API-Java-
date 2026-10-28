@echo off
setlocal
cd /d "%~dp0"
echo Iniciando API Java com banco H2 persistente local na porta 8080.
call mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
exit /b %errorlevel%
