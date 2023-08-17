inherit autotools pkgconfig

DESCRIPTION = "Build crypthelper-mediameta, a helper library\
               to provide mapping between encryption meta and\
               encryptable block devices"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

FILESEXTRAPATHS:prepend := "${WORKSPACE}/system/extras/libcrypthelper-mediameta/:"
SRC_URI   = "file://crypthelper-mediameta"
SRC_URI  += "file://configs/"

S = "${WORKDIR}/crypthelper-mediameta"

CONFIG ?= "default.conf"
CONFIG:cinder  = "cinder.conf"
CONFIG:sxrneo  = "neo.conf"
CONFIG:sm8450p = "waipio.conf"
CONFIG:qrb5165 = "qrb5165.conf"
CONFIG:kalama = "kalama.conf"

EXTRA_OECONF:append = " --with-config=${CONFIG}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PACKAGES =+ "${PN}-lib"
FILES:${PN}-lib   =  "${sysconfdir}/conf/*"
FILES:${PN}-lib  +=  "${libdir}/libcrypthelper_mediameta.so.*  ${libdir}/pkgconfig/*"
