SUMMARY = "Linux mainline kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

inherit kernel fsl-kernel-localversion

S = "${WORKDIR}/git"

DEPENDS += "lzop-native bc-native"

BRANCH = "linux-5.4.y"
LOCALVERSION = "+dr-1.0"
SRCREV = "0c418786cb3aa175823f0172d939679df9ab9a54"
SRC_URI = " \
	git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-stable.git;branch=${BRANCH} \
	file://defconfig \
	file://sdma-imx6q.bin \
"

do_configure_prepend() {
	install -d ${S}/firmware/imx/sdma
	install -m 0644 ${WORKDIR}/sdma-imx6q.bin ${S}/firmware/imx/sdma/
}

COMPATIBLE_MACHINE = "use-mainline-bsp"