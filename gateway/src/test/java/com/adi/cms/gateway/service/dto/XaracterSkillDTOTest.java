package com.adi.cms.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class XaracterSkillDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(XaracterSkillDTO.class);
        XaracterSkillDTO xaracterSkillDTO1 = new XaracterSkillDTO();
        xaracterSkillDTO1.setId(1L);
        XaracterSkillDTO xaracterSkillDTO2 = new XaracterSkillDTO();
        assertThat(xaracterSkillDTO1).isNotEqualTo(xaracterSkillDTO2);
        xaracterSkillDTO2.setId(xaracterSkillDTO1.getId());
        assertThat(xaracterSkillDTO1).isEqualTo(xaracterSkillDTO2);
        xaracterSkillDTO2.setId(2L);
        assertThat(xaracterSkillDTO1).isNotEqualTo(xaracterSkillDTO2);
        xaracterSkillDTO1.setId(null);
        assertThat(xaracterSkillDTO1).isNotEqualTo(xaracterSkillDTO2);
    }
}
