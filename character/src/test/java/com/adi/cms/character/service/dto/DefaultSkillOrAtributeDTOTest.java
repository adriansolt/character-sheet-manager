package com.adi.cms.character.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.character.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DefaultSkillOrAtributeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DefaultSkillOrAtributeDTO.class);
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO1 = new DefaultSkillOrAtributeDTO();
        defaultSkillOrAtributeDTO1.setId(1L);
        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO2 = new DefaultSkillOrAtributeDTO();
        assertThat(defaultSkillOrAtributeDTO1).isNotEqualTo(defaultSkillOrAtributeDTO2);
        defaultSkillOrAtributeDTO2.setId(defaultSkillOrAtributeDTO1.getId());
        assertThat(defaultSkillOrAtributeDTO1).isEqualTo(defaultSkillOrAtributeDTO2);
        defaultSkillOrAtributeDTO2.setId(2L);
        assertThat(defaultSkillOrAtributeDTO1).isNotEqualTo(defaultSkillOrAtributeDTO2);
        defaultSkillOrAtributeDTO1.setId(null);
        assertThat(defaultSkillOrAtributeDTO1).isNotEqualTo(defaultSkillOrAtributeDTO2);
    }
}
