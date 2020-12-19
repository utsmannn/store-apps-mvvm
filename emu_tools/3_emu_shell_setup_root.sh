#!/bin/zsh

adb -e shell su root
adb -e shell chmod 06755 /system/bin/su
adb -e shell /system/bin/su --install
adb -e shell /system/bin/su --daemon&
adb -e shell setenforce 0
exit