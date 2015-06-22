@echo off
set JARNAME=kgainfobot-1.0.jar

set LOGIN=H6Pu9bp
set PASS=NawVUVi

set TIMEOUT=1
set ENDTIME="10:30"

set TASK1=0
set T1STRUCT=812000
set T1DEPART=812003
set T1THEME=11
set T1TIME=11:30
set T1CMNT="..."

set TASK2=0
set T2STRUCT=812000
set T2DEPART=900001
set T2THEME=12
set T2TIME=11:00
set T2CMNT="..."

:loop

if %TASK1% neq 0 goto task2

echo attempt to register in %T1DEPART%
java -jar %JARNAME% login=%LOGIN% pass=%PASS% cmd=register structure=%T1STRUCT% department=%T1DEPART% theme=%T1THEME% time=%T1TIME% comment=%T1CMNT% timeout=%TIMEOUT%
if %ERRORLEVEL% equ 0 set TASK1=1

:task2

if %TASK2% neq 0 goto check

echo attempt to register in %T2DEPART%
java -jar %JARNAME% login=%LOGIN% pass=%PASS% cmd=register structure=%T2STRUCT% department=%T2DEPART% theme=%T2THEME% time=%T2TIME% comment=%T2CMNT% timeout=%TIMEOUT%
if %ERRORLEVEL% equ 0 set TASK2=1

:check

set T=%TIME:~0,5%

if "%T%" geq %ENDTIME% goto end
if %TASK1% equ 0 goto loop
if %TASK2% equ 0 goto loop

:end

echo task1=%TASK1% task2=%TASK2%
