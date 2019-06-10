# Copyright (C) DATA RESPONS
# Released under the MIT license (see COPYING.MIT for the terms)

BRANCH ?= "linux-4.14.y"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-stable.git;branch=${BRANCH} \
           file://0002-imx6-Do-not-mask-interrupts-in-WAIT_CLOCKED-lpm-mode.patch \
           file://0003-Add-Data-Respons-IMX6-machine-file.patch \
           file://0004-Add-NXP-fuse-driver.patch \
           file://0005-gpiolib-Add-call-to-obtain-device-from-gpiod.patch \
           file://0006-VHGW-SMC.patch \
           file://0007-Fix-transaction-handling-bug-leading-to-occ.patch \
           file://0008-Schedule-alarm-on-shutdown.patch \
           file://0009-imx-serial-driver-Add-DT-option-for-non-DMA.patch \
           file://0010-Simple-motion-detect-when-shutdown.patch \
           file://0011-Add-GW-defconfig.patch \
           \
           file://0001-Add-vdt6010-device-tree.patch \
           file://0002-DT-fixes.patch \
           file://0004-Add-vdt6010-defconfig.patch \
           file://0005-DT-compatiblity-to-datarespons-imx.patch \
           file://0006-DT-rework-add-display.patch \
           file://0007-DT-fix-usb-regulator-setup.patch \
           "

DR_PATCH_VERSION = "0.7"

#PV = "${LINUX_VERSION}-dr-${DR_PATCH_VERSION}"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"
DEPENDS += "lzop-native bc-native"
PROVIDES = "virtual/kernel"

inherit kernel
require recipes-kernel/linux/linux-dr.inc

SRCREV ?= "0d7866d54a2c2d708807930c3bfd38ab3ce0550d"
