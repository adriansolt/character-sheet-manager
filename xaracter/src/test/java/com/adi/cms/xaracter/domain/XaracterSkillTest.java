package com.adi.cms.xaracter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.xaracter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class XaracterSkillTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(XaracterSkill.class);
        XaracterSkill xaracterSkill1 = new XaracterSkill();
        xaracterSkill1.setId(1L);
        XaracterSkill xaracterSkill2 = new XaracterSkill();
        xaracterSkill2.setId(xaracterSkill1.getId());
        assertThat(xaracterSkill1).isEqualTo(xaracterSkill2);
        xaracterSkill2.setId(2L);
        assertThat(xaracterSkill1).isNotEqualTo(xaracterSkill2);
        xaracterSkill1.setId(null);
        assertThat(xaracterSkill1).isNotEqualTo(xaracterSkill2);
    }
}
