inherit autotools-brokensep pkgconfig

DESCRIPTION = "Build Android libion"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

# Apply only for echo machine (kernel 6.18)
FILESEXTRAPATHS:prepend:echo := "${THISDIR}/echo:"

FILESEXTRAPATHS:prepend := "${WORKSPACE}/system/core/:"
SRC_URI   = "file://libion"

S = "${WORKDIR}/libion"
DEPENDS += "liblog linux-msm-headers"

EXTRA_OECONF:append = " \
    --disable-static \
    --with-sanitized-headers=${STAGING_INCDIR}/linux-msm/usr/include \
    --with-rootprefix=${root_prefix} \
"

SRC_URI:append:echo = " \
    file://0001-libion-update-ion-abi-for-kernel-6.18.patch;striplevel=2 \
"
# Add core-includes only for echo machine
EXTRA_OECONF:append:echo = " \
    --with-core-includes=${S}/kernel-headers \
"
PACKAGES +="${PN}-test-bin"

FILES:${PN}     = "${libdir}/pkgconfig/* ${libdir}/* ${sysconfdir}/*"
FILES:${PN}-test-bin = "${base_bindir}/*"

PACKAGE_ARCH = "${MACHINE_ARCH}"
