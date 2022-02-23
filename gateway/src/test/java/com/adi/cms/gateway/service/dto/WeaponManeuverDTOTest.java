package com.adi.cms.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WeaponManeuverDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WeaponManeuverDTO.class);
        WeaponManeuverDTO weaponManeuverDTO1 = new WeaponManeuverDTO();
        weaponManeuverDTO1.setId(1L);
        WeaponManeuverDTO weaponManeuverDTO2 = new WeaponManeuverDTO();
        assertThat(weaponManeuverDTO1).isNotEqualTo(weaponManeuverDTO2);
        weaponManeuverDTO2.setId(weaponManeuverDTO1.getId());
        assertThat(weaponManeuverDTO1).isEqualTo(weaponManeuverDTO2);
        weaponManeuverDTO2.setId(2L);
        assertThat(weaponManeuverDTO1).isNotEqualTo(weaponManeuverDTO2);
        weaponManeuverDTO1.setId(null);
        assertThat(weaponManeuverDTO1).isNotEqualTo(weaponManeuverDTO2);
    }
}
