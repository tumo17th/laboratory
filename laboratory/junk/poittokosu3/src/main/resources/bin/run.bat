@echo off

echo PoittoKosu start

rem set year
set /p YEAR="Input target YEAR : "

rem set month
set /p MONTH="Input target MONTH : "

java -jar lib/poittokosu3-1.0.0.jar %YEAR% %MONTH%

echo PoittoKosu end

pause
