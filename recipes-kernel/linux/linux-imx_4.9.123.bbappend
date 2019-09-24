FILESEXTRAPATHS_append := "${THISDIR}/files:${THISDIR}/${PN}-${PV}:"

SRC_URI += "file://X001-DR-mach-imx6q-Add-GPIO-and-watchdog-prestart-detection.patch \
            file://0001-Add-vdt6010-device-tree.patch \
            file://0002-DT-fixes.patch \
            file://0006-DT-rework-add-display.patch \
            file://0007-DT-fix-usb-regulator-setup.patch \
            file://0009-DT-update-1.patch \
            file://0010-DT-update-2.patch \
            file://0011-DT-fix-enet.patch \
            file://0012-DT-support-BMC.patch \
            file://0013-Add-ili2511-driver.patch \
            file://0014-DT-support-ili2511.patch \
            file://0015-Backport-pci-imx6.c-from-mainline-4.9.123.patch \
            file://0016-DT-Add-PCIe-settings-but-leave-disabled.patch \
            file://0017-DT-Partition-SPI-flash.patch \
            file://0018-fsl_updater-wrong-USB-gadget-ID-for-recognizing-targ.patch \
            "

LOCALVERSION ?= "+dr-1"
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

