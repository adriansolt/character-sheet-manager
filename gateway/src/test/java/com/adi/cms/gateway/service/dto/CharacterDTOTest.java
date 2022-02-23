package com.adi.cms.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CharacterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CharacterDTO.class);
        CharacterDTO characterDTO1 = new CharacterDTO();
        characterDTO1.setId(1L);
        CharacterDTO characterDTO2 = new CharacterDTO();
        assertThat(characterDTO1).isNotEqualTo(characterDTO2);
        characterDTO2.setId(characterDTO1.getId());
        assertThat(characterDTO1).isEqualTo(characterDTO2);
        characterDTO2.setId(2L);
        assertThat(characterDTO1).isNotEqualTo(characterDTO2);
        characterDTO1.setId(null);
        assertThat(characterDTO1).isNotEqualTo(characterDTO2);
    }
}
