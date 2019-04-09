G:
cd G:\Android_Automatic_Testing\test app
@echo off
set /P INPUT=Enter AppName: %=%
@echo on
(aapt dump badging %INPUT%.apk)
PAUSE