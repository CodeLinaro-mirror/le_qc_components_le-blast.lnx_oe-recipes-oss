inherit autotools-brokensep pkgconfig systemd

DESCRIPTION = "Powerapp tools"
HOMEPAGE = "http://codeaurora.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

FILESEXTRAPATHS:prepend := "${WORKSPACE}/system/core/:"
SRC_URI = "file://powerapp"

S = "${WORKDIR}/powerapp"

PACKAGECONFIG ?= "glib"
PACKAGECONFIG[glib] = "--with-glib, --without-glib, glib-2.0"

PACKAGES =+ "${PN}-reboot ${PN}-shutdown ${PN}-powerconfig"
FILES:${PN}-reboot = " ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', "${sysconfdir}/initscripts/reboot", "${sysconfdir}/init.d/reboot", d)} "
FILES:${PN}-shutdown = " ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', "${sysconfdir}/initscripts/shutdown", "${sysconfdir}/init.d/shutdown", d)} "
FILES:${PN}-powerconfig = " ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', "${sysconfdir}/initscripts/power_config", "${sysconfdir}/init.d/power_config", d)} "
FILES:${PN} += "/data/*"
FILES:${PN} += "/lib/systemd/*"

# TODO - add depedency on virtual/sh
PROVIDES =+ "${PN}-reboot ${PN}-shutdown ${PN}-powerconfig"

EXTRA_OECONF  = " ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '--with-systemd', '',d)} "
EXTRA_OECONF += "${@bb.utils.contains('MACHINE_FEATURES', 'qti-vm', '--enable-vm-config', '', d)}"

DEPENDS += "glib-2.0"
EXTRA_OECONF += "--with-glib"

# Following machines have individual power_config settings
EXTRA_OECONF:append:neo = " --with-basemachine=${BASEMACHINE}"
EXTRA_OECONF:append:waipio = " --with-basemachine=${BASEMACHINE}"
EXTRA_OECONF:append:sdxpinn = " --with-basemachine=${BASEMACHINE}"
EXTRA_OECONF:append:mdm9607 = " --with-basemachine=${BASEMACHINE}"
EXTRA_OECONF:append:ar-sg1 = " --with-basemachine=${MACHINE}"
EXTRA_OECONF:append:echo= " --with-basemachine=${BASEMACHINE}"
EXTRA_OECONF:append:seraph = " --with-basemachine=${MACHINE}"

do_install:append() {

    if ${@bb.utils.contains('MACHINE', 'seraph', 'true', 'false', d)}; then

        # Move binaries to /usr/sbin
        if [ -d ${D}/sbin ]; then
            mkdir -p ${D}${base_sbindir}
            mv ${D}/sbin/* ${D}${base_sbindir}/
            rmdir ${D}/sbin || true
        fi

        # Move systemd units
        if [ -d ${D}/lib/systemd/system ]; then
            mkdir -p ${D}${systemd_unitdir}/system
            mv ${D}/lib/systemd/system/*.service ${D}${systemd_unitdir}/system/
            rm -rf ${D}/lib
        fi

    fi

    ln ${D}${base_sbindir}/powerapp ${D}${base_sbindir}/sys_reboot
    ln ${D}${base_sbindir}/powerapp ${D}${base_sbindir}/sys_shutdown
}

pkg_postinst:${PN}-reboot () {
        if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'false', 'true', d)}; then
           [ -n "$D" ] && OPT="-r $D" || OPT="-s"
           update-rc.d $OPT -f reboot remove
           update-rc.d $OPT reboot start 99 6 .
	fi
}

pkg_postinst:${PN}-shutdown () {
        if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'false', 'true', d)}; then
           [ -n "$D" ] && OPT="-r $D" || OPT="-s"
           update-rc.d $OPT -f shutdown remove
           update-rc.d $OPT shutdown start 99 0 .
	fi
}

pkg_postinst:${PN}-powerconfig () {
        if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'false', 'true', d)}; then
           [ -n "$D" ] && OPT="-r $D" || OPT="-s"
           update-rc.d $OPT -f power_config remove
           update-rc.d $OPT power_config start 99 2 3 4 5 . stop 50 0 1 6 .
	fi
}

pkg_postinst:${PN}-enableautosleep () {
        if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'false', 'true', d)}; then
          [ -n "$D" ] && OPT="-r $D" || OPT="-s"
           update-rc.d $OPT -f enable-autosleep remove
           update-rc.d $OPT enable_autosleep start 99 2 3 4 5 . stop 50 0 1 6 .
        fi
}

pkg_postinst:${PN} () {
        if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'false', 'true', d)}; then
           [ -n "$D" ] && OPT="-r $D" || OPT="-s"
           update-rc.d $OPT -f reset_reboot_cookie remove
           update-rc.d $OPT reset_reboot_cookie start 55 2 3 4 5 .
        fi
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

SYSTEMD_SERVICE:${PN}  = " reset_reboot_cookie.service "
SYSTEMD_SERVICE:${PN}  += " power_config.service "
SYSTEMD_SERVICE:${PN}  += " enable_autosleep.service "
SYSTEMD_SERVICE:${PN}  += "${@bb.utils.contains('MACHINE_FEATURES','qti-vm',' powerapp.service ','',d)}"
