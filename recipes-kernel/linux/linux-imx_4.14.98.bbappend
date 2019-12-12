FILESEXTRAPATHS_append := "${THISDIR}/files:${THISDIR}/${PN}-${PV}:"

SRC_URI += " \
	file://0001-ILI251X-touch-driver-from-mainline-5.1.21.patch \
    file://0002-fsl_updater-wrong-USB-gadget-ID-for-recognizing-targ.patch \
    file://sdma-imx6q.bin \
"

do_configure_prepend() {
	install -d ${S}/firmware/imx/sdma
	install -m 0644 ${WORKDIR}/sdma-imx6q.bin ${S}/firmware/imx/sdma/
}

LOCALVERSION = "+dr-1.0"