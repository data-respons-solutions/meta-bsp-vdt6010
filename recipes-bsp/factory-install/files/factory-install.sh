#!/bin/sh

die() {
	echo $1
	exit 1	
}

if [ "$#" -lt "1" ]; then
	die "Usage: $0 <u-boot>"
fi

if [ $(id -u) -ne 0 ]; then
	die "Must be run as root"
fi

cd /opt/oe/dr-vdt6010-distro/build/tmp-glibc/deploy/images/vdt6010-factory

# Execute factory image from RAM
imx_usb -c .
