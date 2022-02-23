package com.adi.cms.item.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.item.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class XaracterEquippedWeaponTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(XaracterEquippedWeapon.class);
        XaracterEquippedWeapon xaracterEquippedWeapon1 = new XaracterEquippedWeapon();
        xaracterEquippedWeapon1.setId(1L);
        XaracterEquippedWeapon xaracterEquippedWeapon2 = new XaracterEquippedWeapon();
        xaracterEquippedWeapon2.setId(xaracterEquippedWeapon1.getId());
        assertThat(xaracterEquippedWeapon1).isEqualTo(xaracterEquippedWeapon2);
        xaracterEquippedWeapon2.setId(2L);
        assertThat(xaracterEquippedWeapon1).isNotEqualTo(xaracterEquippedWeapon2);
        xaracterEquippedWeapon1.setId(null);
        assertThat(xaracterEquippedWeapon1).isNotEqualTo(xaracterEquippedWeapon2);
    }
}
