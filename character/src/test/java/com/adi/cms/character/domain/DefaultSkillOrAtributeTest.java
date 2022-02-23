package com.adi.cms.character.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.character.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DefaultSkillOrAtributeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DefaultSkillOrAtribute.class);
        DefaultSkillOrAtribute defaultSkillOrAtribute1 = new DefaultSkillOrAtribute();
        defaultSkillOrAtribute1.setId(1L);
        DefaultSkillOrAtribute defaultSkillOrAtribute2 = new DefaultSkillOrAtribute();
        defaultSkillOrAtribute2.setId(defaultSkillOrAtribute1.getId());
        assertThat(defaultSkillOrAtribute1).isEqualTo(defaultSkillOrAtribute2);
        defaultSkillOrAtribute2.setId(2L);
        assertThat(defaultSkillOrAtribute1).isNotEqualTo(defaultSkillOrAtribute2);
        defaultSkillOrAtribute1.setId(null);
        assertThat(defaultSkillOrAtribute1).isNotEqualTo(defaultSkillOrAtribute2);
    }
}
