@echo off
setlocal
cd /d "%~dp0"
echo Iniciando API Java na porta 8080 com o perfil local e a configuracao do .env.local.
call mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
exit /b %errorlevel%
