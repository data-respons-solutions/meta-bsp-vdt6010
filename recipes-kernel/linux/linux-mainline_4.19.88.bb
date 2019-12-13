SUMMARY = "Linux mainline kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

inherit kernel fsl-kernel-localversion

S = "${WORKDIR}/git"

DEPENDS += "lzop-native bc-native"

BRANCH = "linux-4.19.y"
LOCALVERSION = "+dr-1.0"
SRCREV = "fb683b5e3f53a73e761952735736180939a313df"
SRC_URI += " \
	git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-stable.git;branch=${BRANCH} \
	file://defconfig \
    file://sdma-imx6q.bin \
"

do_configure_prepend() {
	install -d ${S}/firmware/imx/sdma
	install -m 0644 ${WORKDIR}/sdma-imx6q.bin ${S}/firmware/imx/sdma/
}

COMPATIBLE_MACHINE = "(vdt6010|vdt6010-factory)"
