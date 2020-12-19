#!/bin/zsh

cd ~
cd supersu/SuperSU-v2.82-201705271822/x86
adb root
adb remount
adb -e push su.pie /system/bin/su
adb -e shell
su root
cd /system/bin
chmod 06755 su
su --install
su --daemon&
setenforce 0