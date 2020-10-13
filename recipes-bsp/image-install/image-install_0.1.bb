DESCRIPTION = "vdt6010 image installer for fresh installation from USB"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI += " \
	file://image-install.sh \
"

RDEPENDS_${PN} += "blockdev-init nvram"

do_install () {
    install -d ${D}${sbindir}
    install -m 0755 ${WORKDIR}/image-install.sh ${D}${sbindir}/image-install
}