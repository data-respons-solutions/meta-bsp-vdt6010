require u-boot-dr_${PV}.bb

LOCALVERSION_append = "-factory"

PROVIDES = "u-boot-factory"
UBOOT_CONFIG = "factory"

do_install[noexec] = "1"

inherit imx_usb

do_deploy_append() {
	rm ${DEPLOYDIR}/${UBOOT_BINARY}
	rm ${DEPLOYDIR}/${UBOOT_SYMLINK}
	rm ${DEPLOYDIR}/${UBOOT_SYMLINK}-${type}
	rm ${DEPLOYDIR}/${SPL_BINARYNAME}
	rm ${DEPLOYDIR}/${SPL_SYMLINK}
	rm ${DEPLOYDIR}/${SPL_SYMLINK}-${type}
}
