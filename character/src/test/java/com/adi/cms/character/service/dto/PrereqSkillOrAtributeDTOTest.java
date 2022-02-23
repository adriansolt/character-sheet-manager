package com.adi.cms.character.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.character.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrereqSkillOrAtributeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrereqSkillOrAtributeDTO.class);
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO1 = new PrereqSkillOrAtributeDTO();
        prereqSkillOrAtributeDTO1.setId(1L);
        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO2 = new PrereqSkillOrAtributeDTO();
        assertThat(prereqSkillOrAtributeDTO1).isNotEqualTo(prereqSkillOrAtributeDTO2);
        prereqSkillOrAtributeDTO2.setId(prereqSkillOrAtributeDTO1.getId());
        assertThat(prereqSkillOrAtributeDTO1).isEqualTo(prereqSkillOrAtributeDTO2);
        prereqSkillOrAtributeDTO2.setId(2L);
        assertThat(prereqSkillOrAtributeDTO1).isNotEqualTo(prereqSkillOrAtributeDTO2);
        prereqSkillOrAtributeDTO1.setId(null);
        assertThat(prereqSkillOrAtributeDTO1).isNotEqualTo(prereqSkillOrAtributeDTO2);
    }
}
