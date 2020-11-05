SUMMARY = "Linux mainline kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

inherit kernel fsl-kernel-localversion

COMPATIBLE_MACHINE = "(vdt6010|vdt6010-factory)"

S = "${WORKDIR}/git"

DEPENDS += "lzop-native bc-native"

BRANCH = "linux-5.4.y"
LOCALVERSION = "+dr-1.0"
SRCREV = "b300b28b78145b832f1112d77035111e35112cec"

SRC_URI = " \
	git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-stable.git;branch=${BRANCH} \
	file://defconfig \
"
