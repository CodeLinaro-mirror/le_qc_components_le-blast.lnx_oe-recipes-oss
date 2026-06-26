inherit autotools pkgconfig

DESCRIPTION = "Android fs_mgr library and binary"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

FILESEXTRAPATHS:prepend := "${WORKSPACE}/system/core/:"
SRC_URI = "file://fs_mgr"

S = "${UNPACKDIR}/fs_mgr"

DEPENDS += "ext4-utils glib-2.0 libcutils libmincrypt logwrapper"

CFLAGS:append = " -Wno-implicit-function-declaration"
BBCLASSEXTEND = "native"
EXTRA_OECONF = " --with-glib"
