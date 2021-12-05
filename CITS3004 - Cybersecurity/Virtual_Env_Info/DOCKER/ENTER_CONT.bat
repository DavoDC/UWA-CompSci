
@echo off

:: Note: I recommend adding this script as profile in Windows Terminal
:: Command line setting should be the path to the script (may need quotes)



:: Check if docker is running
tasklist /fi "ImageName eq Docker Desktop.exe" /fo csv 2>NUL | find /I "Docker Desktop.exe">NUL

@REM If docker is not running
IF NOT "%ERRORLEVEL%"=="0" (

    @REM Start it
    START "" "C:\Program Files\Docker\Docker\Docker Desktop.exe"

    @REM Wait for it to load (loads alot into RAM)
    echo.
    echo "Press enter only after Docker has loaded in tray..."
    echo.
    @pause
)



:: Start container then enter docker shell 
docker start dave_cont && docker exec -it dave_cont bash
:: - Note: dave_cont is the name of my container
:: - You can list container names using: docker ps -a
:: - You can rename an existing container: https://docs.docker.com/engine/reference/commandline/container_rename/
:: - You can make containers with a certain name using the "--name" switch for the run command




:: AFTER CONTAINER EXITED:

:: Stop container after exiting shell
@REM docker stop dave_cont

:: Exit native shell when docker exited
@REM exit


:: Stop docker desktop (to release RAM)
::TASKKILL /F /IM "%docker_exe%"
