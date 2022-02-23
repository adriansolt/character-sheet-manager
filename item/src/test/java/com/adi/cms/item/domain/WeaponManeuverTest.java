package com.adi.cms.item.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.item.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WeaponManeuverTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WeaponManeuver.class);
        WeaponManeuver weaponManeuver1 = new WeaponManeuver();
        weaponManeuver1.setId(1L);
        WeaponManeuver weaponManeuver2 = new WeaponManeuver();
        weaponManeuver2.setId(weaponManeuver1.getId());
        assertThat(weaponManeuver1).isEqualTo(weaponManeuver2);
        weaponManeuver2.setId(2L);
        assertThat(weaponManeuver1).isNotEqualTo(weaponManeuver2);
        weaponManeuver1.setId(null);
        assertThat(weaponManeuver1).isNotEqualTo(weaponManeuver2);
    }
}
