inherit  autotools

DESCRIPTION = "Boot image creation tool from Android"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

FILESEXTRAPATHS:prepend := "${WORKSPACE}/system/core/:"
SRC_URI = "file://mkbootimg"

S = "${WORKDIR}/${BPN}"

DEPENDS += "libmincrypt-native"

NATIVE_INSTALL_WORKS = "1"

PROVIDES = "virtual/mkbootimg"

BBCLASSEXTEND = "native"
