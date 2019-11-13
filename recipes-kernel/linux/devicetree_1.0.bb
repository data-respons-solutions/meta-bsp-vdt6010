SUMMARY = "Devicetree"
DESCRIPTION = "VDT6010 device tree"
SECTION = "bsp"

inherit devicetree

COMPATIBLE_MACHINE = "(vdt6010|vdt6010-factory)"

SRCREV ?= "93fe5e55232e763ae4436d3e789221f8ad0db001"
SRC_URI = "git://git@bitbucket.datarespons.com:7999/oe-bsp/uboot-vdt6010.git;protocol=ssh;branch=master;"

do_configure_prepend() {
    cp -v ${WORKDIR}/git/arch/arm/dts/vdt6010.dts ${WORKDIR}/datarespons-vdt6010-revA.dts
}

do_install() {
    for DTB_FILE in `ls *.dtb *.dtbo`; do
        install -Dm 0644 ${B}/${DTB_FILE} ${D}/boot/${DTB_FILE}
    done
}

devicetree_do_deploy() {
    for DTB_FILE in `ls *.dtb *.dtbo`; do
        install -Dm 0644 ${B}/${DTB_FILE} ${DEPLOYDIR}/${DTB_FILE}
    done
}

FILES_${PN} = "/boot/*.dtb /boot/devicetree/*.dtbo"