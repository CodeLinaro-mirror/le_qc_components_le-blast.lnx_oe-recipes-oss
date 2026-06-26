inherit autotools pkgconfig native

DESCRIPTION = "EXT4 UTILS"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "libsparse-native libcutils-native libpcre-native"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'libselinux-native', '', d)}"

FILESEXTRAPATHS:prepend := "${WORKSPACE}/system/extras/:"
SRC_URI   = "file://ext4_utils"

S = "${UNPACKDIR}/ext4_utils"

EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES', 'selinux', '--enable-selinux', '', d)}"
