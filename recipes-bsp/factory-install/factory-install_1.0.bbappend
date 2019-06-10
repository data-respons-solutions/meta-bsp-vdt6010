FILESEXTRAPATHS_append := "${THISDIR}/${PN}:"

SRC_URI += " \
	file://emmc_init.py \
	file://hab.py \
"

RDEPENDS_${PN} += "mmc-utils parted util-linux-lsblk"

do_install_append () {
    install -d ${D}${sbindir}
    install -m 0755 ${WORKDIR}/emmc_init.py ${D}${sbindir}
    install -m 0755 ${WORKDIR}/hab.py ${D}${sbindir}
}
