package com.adi.cms.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CharacterSkillTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CharacterSkill.class);
        CharacterSkill characterSkill1 = new CharacterSkill();
        characterSkill1.setId(1L);
        CharacterSkill characterSkill2 = new CharacterSkill();
        characterSkill2.setId(characterSkill1.getId());
        assertThat(characterSkill1).isEqualTo(characterSkill2);
        characterSkill2.setId(2L);
        assertThat(characterSkill1).isNotEqualTo(characterSkill2);
        characterSkill1.setId(null);
        assertThat(characterSkill1).isNotEqualTo(characterSkill2);
    }
}
