inherit autotools systemd pkgconfig

DESCRIPTION = "Scripts for device settings after boot"
HOMEPAGE = "http://codeaurora.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

FILESEXTRAPATHS:prepend := "${WORKSPACE}/system/core/:"
SRC_URI  = "file://rootdir"
SRC_URI += "file://init_post_boot.conf"

S = "${WORKDIR}/rootdir"

PACKAGECONFIG:append:qcs40x = "${@bb.utils.contains('DEBUG_BUILD', '1', "debug", "", d)}"

PACKAGECONFIG:append:genericarmv8 = "${@bb.utils.contains('DEBUG_BUILD', \
                                       '1', " debug", "", d)}"
PACKAGECONFIG:appendr:sa410m = "debug"
PACKAGECONFIG:append:neo = " debug"

PACKAGECONFIG[logrestrict] = "--enable-logrestrict,--disable-logrestrict"
PACKAGECONFIG[debug] = "--enable-debug,--disable-debug"

EXTRA_OECONF:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '--with-systemd', '',d)} \
                        --with-basemachine=${BASEMACHINE} \
                        --with-rootprefix=${root_prefix} \
"

do_compile[noexec]="1"

do_install:append() {
    if ${@bb.utils.contains('EXTRA_OECONF', '--with-systemd', 'true', 'false', d)}; then
        install -d ${D}${systemd_unitdir}/system/multi-user.target.wants/
        ln -sf ${systemd_unitdir}/system/init_post_boot.service \
               ${D}${systemd_unitdir}/system/multi-user.target.wants/init_post_boot.service

        install -d ${D}${systemd_unitdir}/system/ffbm.target.wants/
        ln -sf ${systemd_unitdir}/system/init_post_boot.service \
               ${D}${systemd_unitdir}/system/ffbm.target.wants/init_post_boot.service
        if ${@bb.utils.contains('BASEMACHINE', 'trustedvm', 'true', 'false', d)}; then
               install -m 0744 ${WORKDIR}/init_post_boot.conf -D \
                   ${D}${systemd_unitdir}/system/init_post_boot.service.d/init_post_boot.service.conf
        fi
    fi

    if ${@bb.utils.contains('BASEMACHINE', 'kalama', 'true', 'false', d)}; then
        install -m 755 ${WORKDIR}/rootdir/kalama/init.post_boot_3_4_0.sh ${D}/etc/
        install -m 755 ${WORKDIR}/rootdir/kalama/init.post_boot_3_2_1.sh ${D}/etc/
        install -m 755 ${WORKDIR}/rootdir/kalama/init.post_boot_default_3_4_1.sh ${D}/etc/
        install -m 755 ${WORKDIR}/rootdir/kalama/init.post_boot.sh ${D}/etc/
    fi

    if ${@bb.utils.contains('BASEMACHINE', 'pineapple', 'true', 'false', d)}; then
        install -m 755 ${WORKDIR}/rootdir/pineapple/init.post_boot.sh ${D}/etc/
        install -m 755 ${WORKDIR}/rootdir/pineapple/init.kernel.post_boot-pineapple* ${D}/etc/
    fi

    if ${@bb.utils.contains('BASEMACHINE', 'sun', 'true', 'false', d)}; then
        install -m 755 ${WORKDIR}/rootdir/sun/init.post_boot.sh ${D}/etc/
        install -m 755 ${WORKDIR}/rootdir/sun/init.kernel.post_boot-sun* ${D}/etc/
    fi

    if ${@bb.utils.contains('BASEMACHINE', 'kera', 'true', 'false', d)}; then
        install -m 755 ${WORKDIR}/rootdir/kera/init.post_boot.sh ${D}/etc/
        install -m 755 ${WORKDIR}/rootdir/kera/init.kernel.post_boot-kera* ${D}/etc/
    fi

    if ${@bb.utils.contains('BASEMACHINE', 'vienna', 'true', 'false', d)}; then
        install -m 755 ${WORKDIR}/rootdir/vienna/init.post_boot.sh ${D}/etc/
        install -m 755 ${WORKDIR}/rootdir/vienna/init.kernel.post_boot-vienna.sh ${D}/etc/
        install -m 755 ${WORKDIR}/rootdir/vienna/init.qti.kernel.debug-vienna.sh ${D}/etc/
    fi
    if ${@bb.utils.contains('BASEMACHINE', 'alor', 'true', 'false', d)}; then
        install -m 755 ${WORKDIR}/rootdir/alor/init.post_boot.sh ${D}/etc/
        install -m 755 ${WORKDIR}/rootdir/alor/init.kernel.post_boot-alor* ${D}/etc/
        install -m 755 ${WORKDIR}/rootdir/alor/init.kernel.post_boot-canoe* ${D}/etc/
    fi

}

do_install:append:qti-distro-camera() {
    POST_BOOT_FILE="${D}/etc/init.post_boot.sh"

    if ! grep -q "perf-hal.service" "$POST_BOOT_FILE"; then
        sed -i '$a\
rm -f /data/vendor/perfd/default_values\
systemctl restart perf-hal.service
        ' "$POST_BOOT_FILE"
    fi
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILES:${PN} += "${systemd_unitdir}/system/"
