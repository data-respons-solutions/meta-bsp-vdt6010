DESCRIPTION = "Factory installation script"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit deploy native

SRC_URI = " \
	file://factory-install.sh \
"

do_deploy() {
	install -d ${DEPLOYDIR}
	install -m 0755 ${WORKDIR}/factory-install.sh ${DEPLOYDIR}
}

addtask do_deploy after do_compile
