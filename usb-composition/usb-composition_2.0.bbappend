# Temporary fix until diagD mounts from rootfs

SRC_URI:append:kalama = " file://10-wait-role-switch.conf"

do_install:append:kalama() {
    install -d ${D}${sysconfdir}/usb
    echo 4EE7 > ${D}${sysconfdir}/usb/boot_hsusb_comp

    install -d ${D}${systemd_unitdir}/system/usb.service.d
    install -m 0644 \
        ${UNPACKDIR}/10-wait-role-switch.conf \
        ${D}${systemd_unitdir}/system/usb.service.d/
}

FILES:${PN}:append:kalama = " \
    ${sysconfdir}/usb/boot_hsusb_comp \
    ${systemd_unitdir}/system/usb.service.d/10-wait-role-switch.conf \
"
