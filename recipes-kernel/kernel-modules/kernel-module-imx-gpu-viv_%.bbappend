FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append += " file://vivante.rules"

do_install_append () {
	install -d ${D}${sysconfdir}/udev/rules.d
	install -m 0644 ${WORKDIR}/vivante.rules ${D}${sysconfdir}/udev/rules.d
}

FILES_${PN} += "${sysconfdir}/udev/rules.d"