package com.adi.cms.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CharacterEquippedWeaponDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CharacterEquippedWeaponDTO.class);
        CharacterEquippedWeaponDTO characterEquippedWeaponDTO1 = new CharacterEquippedWeaponDTO();
        characterEquippedWeaponDTO1.setId(1L);
        CharacterEquippedWeaponDTO characterEquippedWeaponDTO2 = new CharacterEquippedWeaponDTO();
        assertThat(characterEquippedWeaponDTO1).isNotEqualTo(characterEquippedWeaponDTO2);
        characterEquippedWeaponDTO2.setId(characterEquippedWeaponDTO1.getId());
        assertThat(characterEquippedWeaponDTO1).isEqualTo(characterEquippedWeaponDTO2);
        characterEquippedWeaponDTO2.setId(2L);
        assertThat(characterEquippedWeaponDTO1).isNotEqualTo(characterEquippedWeaponDTO2);
        characterEquippedWeaponDTO1.setId(null);
        assertThat(characterEquippedWeaponDTO1).isNotEqualTo(characterEquippedWeaponDTO2);
    }
}
