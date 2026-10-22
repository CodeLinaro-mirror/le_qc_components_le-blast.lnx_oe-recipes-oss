inherit autotools pkgconfig

DESCRIPTION = "procrank utility for memory analysis"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "libpagemap libcutils"

FILESEXTRAPATHS:prepend := "${WORKSPACE}/system/extras/:"
SRC_URI = "file://procrank"

S = "${WORKDIR}/procrank"
