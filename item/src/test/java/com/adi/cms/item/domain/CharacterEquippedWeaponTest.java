package com.adi.cms.item.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.item.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CharacterEquippedWeaponTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CharacterEquippedWeapon.class);
        CharacterEquippedWeapon characterEquippedWeapon1 = new CharacterEquippedWeapon();
        characterEquippedWeapon1.setId(1L);
        CharacterEquippedWeapon characterEquippedWeapon2 = new CharacterEquippedWeapon();
        characterEquippedWeapon2.setId(characterEquippedWeapon1.getId());
        assertThat(characterEquippedWeapon1).isEqualTo(characterEquippedWeapon2);
        characterEquippedWeapon2.setId(2L);
        assertThat(characterEquippedWeapon1).isNotEqualTo(characterEquippedWeapon2);
        characterEquippedWeapon1.setId(null);
        assertThat(characterEquippedWeapon1).isNotEqualTo(characterEquippedWeapon2);
    }
}
