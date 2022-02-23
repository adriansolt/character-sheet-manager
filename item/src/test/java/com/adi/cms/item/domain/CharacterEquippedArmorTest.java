package com.adi.cms.item.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.item.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CharacterEquippedArmorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CharacterEquippedArmor.class);
        CharacterEquippedArmor characterEquippedArmor1 = new CharacterEquippedArmor();
        characterEquippedArmor1.setId(1L);
        CharacterEquippedArmor characterEquippedArmor2 = new CharacterEquippedArmor();
        characterEquippedArmor2.setId(characterEquippedArmor1.getId());
        assertThat(characterEquippedArmor1).isEqualTo(characterEquippedArmor2);
        characterEquippedArmor2.setId(2L);
        assertThat(characterEquippedArmor1).isNotEqualTo(characterEquippedArmor2);
        characterEquippedArmor1.setId(null);
        assertThat(characterEquippedArmor1).isNotEqualTo(characterEquippedArmor2);
    }
}
