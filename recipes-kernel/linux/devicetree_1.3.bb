SUMMARY = "Devicetree"
DESCRIPTION = "VDT6010 device tree"
SECTION = "bsp"

inherit devicetree

COMPATIBLE_MACHINE = "(vdt6010|vdt6010-factory)"

SRCREV ?= "e09f37f7f1fd56d93d1cc3149a4809a67ec06cea"
SRC_URI = "git://git@github.com/data-respons-solutions/uboot-vdt6010.git;protocol=ssh;branch=master;"

S = "${WORKDIR}/git/arch/arm/dts"

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