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

PACKAGECONFIG ??= "\
    ${@bb.utils.filter('DISTRO_FEATURES', 'systemd', d)} \
"

PACKAGECONFIG[systemd] = "--with-tmpfilesdir=${sysconfdir}/tmpfiles.d/, --with-tmpfilesdir=''"

EXTRA_OECONF = " \
        ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '--with-systemd', '',d)} \
        --with-rootprefix=${root_prefix} \
"

do_install:append() {
    install -b -m 0644 /dev/null -D ${D}${sysconfdir}/build.prop

    if ${@bb.utils.contains('DISTRO_FEATURES', 'full-disk-encryption', 'true', 'false', d)}; then

        # Add DefaultDependencies=no
        sed -i '/^Before=/a DefaultDependencies=no' ${D}${systemd_system_unitdir}/leprop.service

        # Change WantedBy target
        sed -i 's/^WantedBy=local-fs/WantedBy=local-fs-pre/' ${D}${systemd_system_unitdir}/leprop.service

    fi
}

do_install:append:qti-distro-camera() {
    SERVICE_FILE="${D}${systemd_system_unitdir}/leprop.service"

    if [ -f "$SERVICE_FILE" ]; then
        if ! grep -q '^DefaultDependencies=no' "$SERVICE_FILE"; then
            sed -i '/^Before=/a DefaultDependencies=no' "$SERVICE_FILE"
        fi
    fi
}

SYSTEMD_SERVICE:${PN}  = " leprop.service "

FILES:${PN} += "${systemd_unitdir}/system/"

EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES', 'selinux', '--enable-selinux', '', d)}"
