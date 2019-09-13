#!/bin/bash

die() {
	echo $1
	exit 1	
}

if [ "$#" -lt "2" ]; then
	die "Usage: $0 <spl> <u-boot>"
fi

if [ $(id -u) -ne 0 ]; then
	die "Must be run as root"
fi

spl="$1"
uboot="$2"

# Here we get a copy of the /dev folder before our scsi device is initiated
sg_before=$(ls /dev/sg* | grep "sg")

# Load and execute factory image from RAM
imx_usb -c . || die "Failed loading factory image to RAM"

# Find scsi generic interface of target
echo ""
echo -n "Waiting for target to boot"

i=1
timeout=20
while [ $i -le ${timeout} ]; do
	echo -n "."
	sg_after=$(ls /dev/sg* | grep "sg")
	diff_list=$(diff -C 1 <(cat <<<"${sg_before}") <(cat <<<"${sg_after}"))
	if [ $? -eq 1 ]; then
		device=$(echo "${diff_list}" | grep '+ /dev/sg' | cut -c 3-)
		break
	fi
	device="NONE"
	sleep 1
	i=$((i+1))
done

echo ""
if [ "${device}" == "NONE" ]; then
	die "Finding device timed out"
fi
echo "Found device ${device}"

echo "Flashing uboot"
utp_com -d ${device} -c "send" -f ${spl} || die "Failed transferring spl binary"
utp_com -d ${device} -c "$ cp \$FILE /tmp/spl" || die "Failed moving spl binary to tmp"
utp_com -d ${device} -c "send" -f ${uboot} || die "Failed transferring uboot binary"
utp_com -d ${device} -c "$ cp \$FILE /tmp/uboot" || die "Failed moving uboot binary to tmp"
utp_com -d ${device} -c "$ flash-uboot --spl /tmp/spl --uboot /tmp/uboot --gpio gpio101" || die "Failed flashing"

echo ""
echo "FACTORY INSTALLATION SUCCESSFULL!"
exit 0
