inherit native autotools pkgconfig

DESCRIPTION = "Minimalistic encryption library from Android"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "BSD-3-Clause & Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
                    file://${COREBASE}/meta/files/common-licenses/\
BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

FILESPATH =+ "${WORKSPACE}/system/core/:"
SRC_URI   = "file://libmincrypt \
             file://include"

S = "${WORKDIR}/libmincrypt"
