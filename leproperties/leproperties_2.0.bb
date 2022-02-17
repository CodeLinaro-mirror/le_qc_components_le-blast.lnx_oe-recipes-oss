inherit autotools pkgconfig systemd

DESCRIPTION = "Andorid like properties managment for LE"
LICENSE = "BSD-3-Clause & Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
                    file://${COREBASE}/meta/files/common-licenses/\
BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

FILESEXTRAPATHS_prepend := "${WORKSPACE}/system/core/:"
SRC_URI = "file://leproperties"

S = "${WORKDIR}/leproperties"

DEPENDS += "libselinux libcutils liblog"

EXTRA_OECONF = "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '--with-systemd', '',d)}"

do_install_append() {
    if ${@bb.utils.contains('EXTRA_OECONF', '--with-systemd', 'true', 'false', d)}; then
        install -b -m 0644 /dev/null -D ${D}${sysconfdir}/build.prop
        install -d ${D}${systemd_unitdir}/system/
        install -d ${D}${systemd_unitdir}/system/multi-user.target.wants/
        install -d ${D}${systemd_unitdir}/system/ffbm.target.wants/
        ln -sf ${systemd_unitdir}/system/leprop.service \
               ${D}${systemd_unitdir}/system/multi-user.target.wants/leprop.service
        ln -sf ${systemd_unitdir}/system/leprop.service \
               ${D}${systemd_unitdir}/system/ffbm.target.wants/leprop.service
    fi
}

FILES_${PN} += "${systemd_unitdir}/system/"
