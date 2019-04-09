@echo off
set /P INPUT=Enter uixName: %=%
adb shell screencap -p /data/local/tmp/%INPUT%.png
(adb pull /data/local/tmp/%INPUT%.png G:\Android_Automatic_Testing\screen_dump)
PAUSE