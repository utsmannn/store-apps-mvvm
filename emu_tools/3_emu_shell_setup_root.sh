#!/bin/zsh

su root
cd /system/bin
chmod 06755 su
su --install
su --daemon&
setenforce 0