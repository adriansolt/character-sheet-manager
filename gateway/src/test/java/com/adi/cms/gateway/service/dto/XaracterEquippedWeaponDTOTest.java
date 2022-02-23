package com.adi.cms.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class XaracterEquippedWeaponDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(XaracterEquippedWeaponDTO.class);
        XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO1 = new XaracterEquippedWeaponDTO();
        xaracterEquippedWeaponDTO1.setId(1L);
        XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO2 = new XaracterEquippedWeaponDTO();
        assertThat(xaracterEquippedWeaponDTO1).isNotEqualTo(xaracterEquippedWeaponDTO2);
        xaracterEquippedWeaponDTO2.setId(xaracterEquippedWeaponDTO1.getId());
        assertThat(xaracterEquippedWeaponDTO1).isEqualTo(xaracterEquippedWeaponDTO2);
        xaracterEquippedWeaponDTO2.setId(2L);
        assertThat(xaracterEquippedWeaponDTO1).isNotEqualTo(xaracterEquippedWeaponDTO2);
        xaracterEquippedWeaponDTO1.setId(null);
        assertThat(xaracterEquippedWeaponDTO1).isNotEqualTo(xaracterEquippedWeaponDTO2);
    }
}
