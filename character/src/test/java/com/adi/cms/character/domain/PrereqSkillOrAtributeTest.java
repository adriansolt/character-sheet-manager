package com.adi.cms.character.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.character.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrereqSkillOrAtributeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrereqSkillOrAtribute.class);
        PrereqSkillOrAtribute prereqSkillOrAtribute1 = new PrereqSkillOrAtribute();
        prereqSkillOrAtribute1.setId(1L);
        PrereqSkillOrAtribute prereqSkillOrAtribute2 = new PrereqSkillOrAtribute();
        prereqSkillOrAtribute2.setId(prereqSkillOrAtribute1.getId());
        assertThat(prereqSkillOrAtribute1).isEqualTo(prereqSkillOrAtribute2);
        prereqSkillOrAtribute2.setId(2L);
        assertThat(prereqSkillOrAtribute1).isNotEqualTo(prereqSkillOrAtribute2);
        prereqSkillOrAtribute1.setId(null);
        assertThat(prereqSkillOrAtribute1).isNotEqualTo(prereqSkillOrAtribute2);
    }
}
