package com.adi.cms.item.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.item.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class XaracterEquippedArmorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(XaracterEquippedArmor.class);
        XaracterEquippedArmor xaracterEquippedArmor1 = new XaracterEquippedArmor();
        xaracterEquippedArmor1.setId(1L);
        XaracterEquippedArmor xaracterEquippedArmor2 = new XaracterEquippedArmor();
        xaracterEquippedArmor2.setId(xaracterEquippedArmor1.getId());
        assertThat(xaracterEquippedArmor1).isEqualTo(xaracterEquippedArmor2);
        xaracterEquippedArmor2.setId(2L);
        assertThat(xaracterEquippedArmor1).isNotEqualTo(xaracterEquippedArmor2);
        xaracterEquippedArmor1.setId(null);
        assertThat(xaracterEquippedArmor1).isNotEqualTo(xaracterEquippedArmor2);
    }
}
