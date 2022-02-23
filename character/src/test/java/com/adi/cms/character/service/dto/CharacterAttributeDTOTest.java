package com.adi.cms.character.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.character.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CharacterAttributeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CharacterAttributeDTO.class);
        CharacterAttributeDTO characterAttributeDTO1 = new CharacterAttributeDTO();
        characterAttributeDTO1.setId(1L);
        CharacterAttributeDTO characterAttributeDTO2 = new CharacterAttributeDTO();
        assertThat(characterAttributeDTO1).isNotEqualTo(characterAttributeDTO2);
        characterAttributeDTO2.setId(characterAttributeDTO1.getId());
        assertThat(characterAttributeDTO1).isEqualTo(characterAttributeDTO2);
        characterAttributeDTO2.setId(2L);
        assertThat(characterAttributeDTO1).isNotEqualTo(characterAttributeDTO2);
        characterAttributeDTO1.setId(null);
        assertThat(characterAttributeDTO1).isNotEqualTo(characterAttributeDTO2);
    }
}
