require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc


IS_FACTORY = "0"
IS_FACTORY_factory = "1"
inherit fsl-u-boot-localversion ${@oe.utils.conditional('IS_FACTORY','1','imx6_usb','',d)}

DEPENDS += "bc-native dtc-native"

SRCREV_FORMAT = "uboot_common_system"
SRCREV_uboot = "3c99166441bf3ea325af2da83cfe65430b49c066"
SRCREV_common = "ca50b70651cfd52e17beb6a8fd1a1eabee603de5"
SRCREV_system = "51e94af7cd80fce0a8b52068fc6a0b19a2ae88d4"

SRC_URI = "git://git.denx.de/u-boot.git;name=uboot \
           git://git@github.com/data-respons-solutions/uboot-vdt6010.git;branch=master;protocol=ssh;destsuffix=git/board/datarespons/vdt6010;name=system \
           gitsm://git@github.com/data-respons-solutions/uboot-common.git;branch=master;protocol=ssh;destsuffix=git/board/datarespons/common;name=common \
           file://0001-Add-links-for-vdt6010.patch \
           file://0002-Add-vdt6010-to-mx6-Kconfig.patch \
           file://0003-Add-dts-links-for-vdt6010.patch \
           file://0004-Add-vdt6010-to-dts-makefile.patch \
           file://0005-Add-link-for-datarespons.h.patch \
           "

LOCALVERSION = "+dr-2.3"

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
IMX6_USB_DTB_LOADADDR = "0x11000000"
IMX6_USB_ZIMAGE_LOADADDR = "0x12000000"
IMX6_USB_INITRD_LOADADDR = "0x12C00000"

FILES_${PN}_append_factory += "/boot"
