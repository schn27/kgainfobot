@echo off
set JARNAME=kgainfobot-1.0.jar
set LOGIN=H6Pu9bp
set PASS=NawVUVi
set TIMEOUT=30
set ENDTIME=10:05:00.00

set TASK1=0
set TASK2=0

:loop

if %TASK1% neq 0 goto task2

echo attempt to register in 812003
java -jar %JARNAME% login=%LOGIN% pass=%PASS% cmd=register structure=812000 department=812003 theme=11 time=11:30 comment="..." timeout=%TIMEOUT%
if %ERRORLEVEL% equ 0 set TASK1=1

:task2

if %TASK2% neq 0 goto check

echo attempt to register in 900001
java -jar %JARNAME% login=%LOGIN% pass=%PASS% cmd=register structure=812000 department=900001 theme=12 time=11:00 comment="..." timeout=%TIMEOUT%
if %ERRORLEVEL% equ 0 set TASK2=1

:check

rem if %TIME% gtr %ENDTIME% goto end
if %TASK1% equ 0 goto loop
if %TASK2% equ 0 goto loop

:end

echo task1=%TASK1% task2=%TASK2%
