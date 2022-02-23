package com.adi.cms.character.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.character.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CharacterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Character.class);
        Character character1 = new Character();
        character1.setId(1L);
        Character character2 = new Character();
        character2.setId(character1.getId());
        assertThat(character1).isEqualTo(character2);
        character2.setId(2L);
        assertThat(character1).isNotEqualTo(character2);
        character1.setId(null);
        assertThat(character1).isNotEqualTo(character2);
    }
}
