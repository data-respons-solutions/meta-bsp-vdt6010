FILESEXTRAPATHS_append := "${THISDIR}/files:${THISDIR}/${PN}-${PV}:"

SRC_URI += " \
    file://X001-DR-mach-imx6q-Add-GPIO-and-watchdog-prestart-detection.patch \
    file://0001-Add-ili2511-driver.patch \
    file://0002-fsl_updater-wrong-USB-gadget-ID-for-recognizing-targ.patch \
"

LOCALVERSION = "+dr-1.0"