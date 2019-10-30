FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRVREV_FORMAT = "uboot_common_system"
SRCREV_uboot = "${SRCREV}"
SRCREV_common = "6d0d78fc9d16c011e3c73972ca3345997f2f46b5"
SRCREV_system = "dfd835dade3f31d62d86ef2c217408dad5c28518"

SRC_URI = " \
	git://git.denx.de/u-boot.git;name=uboot \
	git://git@bitbucket.datarespons.com:7999/oe-bsp/uboot-common.git;protocol=ssh;branch=master;destsuffix=git/board/datarespons/common;name=common \
	git://git@bitbucket.datarespons.com:7999/oe-bsp/uboot-vdt6010.git;protocol=ssh;branch=master;destsuffix=git/board/datarespons/vdt6010;name=system \
	file://0001-Add-links-for-vdt6010.patch \
	file://0002-Add-vdt6010-to-mx6-Kconfig.patch \
"

LOCALVERSION = "+dr-1.1"

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
