
DESCRIPTION = "Boot image creation tool from Android"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

FILESEXTRAPATHS:prepend := "${WORKSPACE}/system/core/:"
SRC_URI = "file://mkbootimg"

S = "${UNPACKDIR}/${BPN}"

DEPENDS += "libmincrypt-native"

PROVIDES = "virtual/mkbootimg"

BBCLASSEXTEND = "native"

do_compile:class-target[noexec] = "1"
do_configure[noexec] = "1"
MY_PN = "mkbootimg"

EXTRA_OEMAKE = "INCLUDES='-Imincrypt' LIBS='-L${libdir} -lmincrypt'"

do_compile:class-native () {
    cp -rf ${WORKSPACE}/system/core/${MY_PN}/* ${S}
    cd ${S}
	oe_runmake
    cd -
}

do_compile:class-target () {
    :
}

do_install:class-native () {
    install -d ${D}/${bindir}
    cp ${S}/mkbootimg ${D}/${bindir}
}

do_install:class-target() {
    install -d ${D}${includedir}
    install -d ${D}${includedir}/bootimg
    install ${S}/bootimg.h ${D}${includedir}/bootimg.h
}
