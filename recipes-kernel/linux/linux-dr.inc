# Copyright (C) 2012, 2014 O.S. Systems Software LTDA.
# Released under the MIT license (see COPYING.MIT for the terms)

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit kernel

# Put a local version until we have a true SRCREV to point to
LOCALVERSION ?= "+dr"
SCMVERSION ?= "y"
DR_PATCH_VERSION ?= "unpatched"
BRANCH ?= "master"

MACHINE_KERNEL_CONFIG ?= "datarespons-vdt6010_defconfig"

S = "${WORKDIR}/git"

#KERNEL_CONFIG_COMMAND ?= "oe_runmake_call -C ${S} O=${B} ${MACHINE_KERNEL_CONFIG}"


kernel_conf_variable() {
	CONF_SED_SCRIPT="$CONF_SED_SCRIPT /CONFIG_$1[ =]/d;"
	if test "$2" = "n"
	then
		echo "# CONFIG_$1 is not set" >> ${B}/.config
	else
		echo "CONFIG_$1=$2" >> ${B}/.config
	fi
}

do_configure_prepend() {
	echo "" > ${B}/.config
	CONF_SED_SCRIPT=""

	kernel_conf_variable LOCALVERSION "\"${LOCALVERSION}\""
	kernel_conf_variable LOCALVERSION_AUTO y

	 cp '${S}/arch/arm/configs/${MACHINE_KERNEL_CONFIG}' '${B}/.config'

	if [ "${SCMVERSION}" = "y" ]; then
		# Add GIT revision to the local version
		head=`git --git-dir=${S}/.git rev-parse --verify --short HEAD 2> /dev/null`
		printf "%s%s%s" -pl-${DR_PATCH_VERSION} +g $head > ${S}/.scmversion
	fi
}
