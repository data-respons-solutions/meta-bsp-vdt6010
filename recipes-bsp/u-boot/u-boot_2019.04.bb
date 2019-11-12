require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

inherit fsl-u-boot-localversion ${@oe.utils.conditional('MACHINE','vdt6010-factory','imx_usb','',d)}

DEPENDS += "bc-native dtc-native"

SRCREV_FORMAT = "uboot_common_system"
SRCREV_uboot = "3c99166441bf3ea325af2da83cfe65430b49c066"
SRCREV_common = "0c4f96887d7107c5150909195bd5358164777eab"
SRCREV_system = "93fe5e55232e763ae4436d3e789221f8ad0db001"

SRC_URI = "git://git.denx.de/u-boot.git;name=uboot \
           gitsm://git@bitbucket.datarespons.com:7999/oe-bsp/uboot-common.git;protocol=ssh;branch=master;destsuffix=git/board/datarespons/common;name=common \
           git://git@bitbucket.datarespons.com:7999/oe-bsp/uboot-vdt6010.git;protocol=ssh;branch=master;destsuffix=git/board/datarespons/vdt6010;name=system \
           file://0001-Add-links-for-vdt6010.patch \
           file://0002-Add-vdt6010-to-mx6-Kconfig.patch \
           file://0003-Add-dts-links-for-vdt6010.patch \
           file://0004-Add-vdt6010-to-dts-makefile.patch \
           file://0005-Add-link-for-datarespons.h.patch \
           "

LOCALVERSION = "+dr-2.0"

EXTRA_OEMAKE += 'V=0'

PV_append = "${LOCALVERSION}"

UBOOT_BINARY = "u-boot-ivt.img"
SPL_BINARY = "SPL"

RPROVIDES_${PN} = "u-boot"

do_install_append() {
	install -d ${D}/boot
	install -m 644 ${B}/${config}/${UBOOT_BINARY}.log ${D}/boot/
	install -m 644 ${B}/${config}/${SPL_BINARY}.log ${D}/boot/
}


UBOOT_CONFIG = "production"
