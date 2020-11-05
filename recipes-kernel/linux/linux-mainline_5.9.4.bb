SUMMARY = "Linux mainline kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel fsl-kernel-localversion

COMPATIBLE_MACHINE = "(vdt6010|vdt6010-factory)"

S = "${WORKDIR}/git"

DEPENDS += "lzop-native bc-native"

BRANCH = "linux-5.9.y"
LOCALVERSION = "+dr-1.0"
SRCREV = "ad2f76f72d803692f83312b4b5b0ffc03e570698"

SRC_URI = " \
	git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-stable.git;branch=${BRANCH} \
	file://defconfig \
"
