inherit autotools pkgconfig systemd

DESCRIPTION = "Andorid like properties managment for LE"
LICENSE = "BSD-3-Clause & Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
                    file://${COREBASE}/meta/files/common-licenses/\
BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

FILESEXTRAPATHS:prepend := "${WORKSPACE}/system/core/:"
SRC_URI = "file://leproperties"

S = "${WORKDIR}/leproperties"

DEPENDS += "libcutils liblog"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'libselinux', '', d)}"

EXTRA_OECONF = " \
        ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '--with-systemd', '',d)} \
        --with-rootprefix=${root_prefix} \
"

do_install:append() {
    install -b -m 0644 /dev/null -D ${D}${sysconfdir}/build.prop
}

SYSTEMD_SERVICE:${PN}  = " leprop.service "

FILES:${PN} += "${systemd_unitdir}/system/"
