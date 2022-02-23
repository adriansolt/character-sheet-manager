package com.adi.cms.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CharacterEquippedArmorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CharacterEquippedArmorDTO.class);
        CharacterEquippedArmorDTO characterEquippedArmorDTO1 = new CharacterEquippedArmorDTO();
        characterEquippedArmorDTO1.setId(1L);
        CharacterEquippedArmorDTO characterEquippedArmorDTO2 = new CharacterEquippedArmorDTO();
        assertThat(characterEquippedArmorDTO1).isNotEqualTo(characterEquippedArmorDTO2);
        characterEquippedArmorDTO2.setId(characterEquippedArmorDTO1.getId());
        assertThat(characterEquippedArmorDTO1).isEqualTo(characterEquippedArmorDTO2);
        characterEquippedArmorDTO2.setId(2L);
        assertThat(characterEquippedArmorDTO1).isNotEqualTo(characterEquippedArmorDTO2);
        characterEquippedArmorDTO1.setId(null);
        assertThat(characterEquippedArmorDTO1).isNotEqualTo(characterEquippedArmorDTO2);
    }
}
