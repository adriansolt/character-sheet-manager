package com.adi.cms.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CharacterAttributeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CharacterAttribute.class);
        CharacterAttribute characterAttribute1 = new CharacterAttribute();
        characterAttribute1.setId(1L);
        CharacterAttribute characterAttribute2 = new CharacterAttribute();
        characterAttribute2.setId(characterAttribute1.getId());
        assertThat(characterAttribute1).isEqualTo(characterAttribute2);
        characterAttribute2.setId(2L);
        assertThat(characterAttribute1).isNotEqualTo(characterAttribute2);
        characterAttribute1.setId(null);
        assertThat(characterAttribute1).isNotEqualTo(characterAttribute2);
    }
}
