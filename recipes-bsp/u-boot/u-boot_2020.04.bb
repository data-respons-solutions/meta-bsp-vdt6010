require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc


IS_FACTORY = "0"
IS_FACTORY_factory = "1"
inherit fsl-u-boot-localversion ${@oe.utils.conditional('IS_FACTORY','1','imx6_usb','',d)}

DEPENDS += "bc-native dtc-native"

SRCREV_FORMAT = "uboot_common_system"
SRCREV_uboot = "36fec02b1f90b92cf51ec531564f9284eae27ab4"
SRCREV_common = "06f1253a01cf3ffc76f4af2b922b3df8b1e1f480"
SRCREV_system = "59539859079b9889917d5cb20ca372810833e2a7"

SRC_URI = "git://git.denx.de/u-boot.git;name=uboot \
           git://git@github.com/data-respons-solutions/uboot-vdt6010.git;branch=master;protocol=ssh;destsuffix=git/board/datarespons/vdt6010;name=system \
           gitsm://git@github.com/data-respons-solutions/uboot-common.git;branch=master;protocol=ssh;destsuffix=git/board/datarespons/common;name=common \
           file://0001-Add-links-for-vdt6010.patch \
           file://0002-Add-vdt6010-to-mx6-Kconfig.patch \
           file://0003-Add-dts-links-for-vdt6010.patch \
           file://0004-Add-vdt6010-to-dts-makefile.patch \
           file://0005-Add-link-for-datarespons.h.patch \
           "

LOCALVERSION = "+dr-2.6"

EXTRA_OEMAKE += 'V=0'

PV_append = "${LOCALVERSION}"

UBOOT_BINARY = "u-boot-ivt.img"
SPL_BINARY = "SPL"

RPROVIDES_${PN} = "u-boot"

UBOOT_CONFIG = "production"

# Create imx_usb loader configs for factory machine
python () {
    if d.getVar('IS_FACTORY', True) == '1':
        bb.build.addtask('do_imx6_usb', 'do_install', 'do_compile', d)
}

do_install_append_factory() {
	for f in ${B}/imx6_usb/*; do
		install -m 0644 ${f} ${D}/boot/;
	done
}

do_deploy_append_factory() {
	for f in ${B}/imx6_usb/*; do
		install -m 0644 ${f} ${DEPLOYDIR}/;
	done
}

IMX6_USB_DIR = "${B}/imx6_usb"
IMX6_USB_RAW_VID = "0x15a2"
IMX6_USB_RAW_PID = "0x0054"
IMX6_USB_PID = "0x2110"
IMX6_USB_DTB = "${FACTORY_DEVICETREE}"
IMX6_USB_DTB_LOADADDR = "0x20000000"
IMX6_USB_ZIMAGE_LOADADDR = "0x12000000"
IMX6_USB_INITRD_LOADADDR = "0x20400000"

FILES_${PN}_append_factory += "/boot"
