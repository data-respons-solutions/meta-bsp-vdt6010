FILESEXTRAPATHS_append := "${THISDIR}/files:${THISDIR}/${PN}-${PV}:"

SRC_URI += " \
	file://0001-ILI251X-touch-driver-from-mainline-5.1.21.patch \
    file://0002-fsl_updater-wrong-USB-gadget-ID-for-recognizing-targ.patch \
"

LOCALVERSION = "+dr-1.0"