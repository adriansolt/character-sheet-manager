package com.adi.cms.character.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.character.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CharacterSkillDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CharacterSkillDTO.class);
        CharacterSkillDTO characterSkillDTO1 = new CharacterSkillDTO();
        characterSkillDTO1.setId(1L);
        CharacterSkillDTO characterSkillDTO2 = new CharacterSkillDTO();
        assertThat(characterSkillDTO1).isNotEqualTo(characterSkillDTO2);
        characterSkillDTO2.setId(characterSkillDTO1.getId());
        assertThat(characterSkillDTO1).isEqualTo(characterSkillDTO2);
        characterSkillDTO2.setId(2L);
        assertThat(characterSkillDTO1).isNotEqualTo(characterSkillDTO2);
        characterSkillDTO1.setId(null);
        assertThat(characterSkillDTO1).isNotEqualTo(characterSkillDTO2);
    }
}
