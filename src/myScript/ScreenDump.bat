@echo off
set /P INPUT=Enter uixName: %=%
adb shell /system/bin/uiautomator dump --compressed  /data/local/tmp/%INPUT%.xml
(adb pull /data/local/tmp/%INPUT%.xml G:\Android_Automatic_Testing\screen_dump)
PAUSE