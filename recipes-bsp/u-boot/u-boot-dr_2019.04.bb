require recipes-bsp/u-boot/u-boot-fslc_${PV}.bb

SRC_URI = "git://github.com/Freescale/u-boot-fslc.git;branch=${SRCBRANCH} \
           file://X001-Add-Datarespons-common.patch \
           file://0001-Add-VDT6010.patch \
           file://0002-Add-factory-defconfig.patch \
           file://0003-SPI-nor-flash-boot-added.patch \
           file://0004-Add-display.patch \
           file://0005-Fix-FEC-and-lvds-display.patch \
           file://0006-Disable-DEBUG.patch \
           file://0007-Set-backlight-PWM-freq-to-1KHz.patch \
           file://0008-Disable-pullup-on-LAN8710-IRQ.patch \
           file://0009-Define-CONFIG_SPI_MEM-to-enable-build-in-2019.11.patch \
           file://0010-Remove-double-enabled-kernel-append-console.patch \
           file://0011-Adjust-SPI-Flash-layout-fix-gadget-VID-PID-fix-CONFI.patch \
           file://0012-Adjust-DDR-IOMUXC_SW_PAD_CTL.patch \
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
