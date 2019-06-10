SUMMARY = "VDT6010 BMC kernel module"
DESCRIPTION = "Board controller driver"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit module

SRC_URI = " \
	file://Makefile \
	file://blta_stm32l051k8.c \
"

S = "${WORKDIR}"

#MACHINE_KERNEL_PR_append = "b"
#PR = "${MACHINE_KERNEL_PR}"


#EXTRA_OEMAKE += 'KERNEL_SRC="${STAGING_KERNEL_DIR}" PREFIX=${prefix}'
#MAKE_TARGETS = "all"

#do_install_prepend() {
#    cp ${B}/Module.symvers ${B}/ || true
#}

COMPATIBLE_MACHINE = "vdt6010"
KERNEL_MODULE_AUTOLOAD += "blta_stm32l051k8"

RPROVIDES_${PN} += "kernel-module-bmc"
