DESCRIPTION = "Initialize system"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://linuxrc"

RDEPENDS_${PN} = "u-boot flash-uboot flash-fuse"

S = "${WORKDIR}"

do_install () {
    install -m 0755 ${WORKDIR}/linuxrc ${D}
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
FILES_${PN} += "/linuxrc"
