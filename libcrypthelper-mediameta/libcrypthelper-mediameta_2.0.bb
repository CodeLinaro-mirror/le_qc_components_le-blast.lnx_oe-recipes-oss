inherit autotools pkgconfig

DESCRIPTION = "Build crypthelper-mediameta, a helper library\
               to provide mapping between encryption meta and\
               encryptable block devices"

LICENSE = "BSD-Source-Code"

LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=fe8b41221d7524c70688f7d059ff6d87"

FILESEXTRAPATHS:prepend := "${WORKSPACE}/system/extras/libcrypthelper-mediameta/:"
SRC_URI   = "file://crypthelper-mediameta"
SRC_URI  += "file://configs/"

S = "${WORKDIR}/crypthelper-mediameta"

CONFIG ?= "default.conf"
CONFIG:cinder  = "cinder.conf"
CONFIG:sxrneo  = "neo.conf"
CONFIG:sm8450p = "waipio.conf"

EXTRA_OECONF:append = " --with-config=${CONFIG}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PACKAGES =+ "${PN}-lib"
FILES:${PN}-lib   =  "${sysconfdir}/conf/*"
FILES:${PN}-lib  +=  "${libdir}/libcrypthelper_mediameta.so.*  ${libdir}/pkgconfig/*"
