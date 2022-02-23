package com.adi.cms.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class XaracterEquippedArmorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(XaracterEquippedArmorDTO.class);
        XaracterEquippedArmorDTO xaracterEquippedArmorDTO1 = new XaracterEquippedArmorDTO();
        xaracterEquippedArmorDTO1.setId(1L);
        XaracterEquippedArmorDTO xaracterEquippedArmorDTO2 = new XaracterEquippedArmorDTO();
        assertThat(xaracterEquippedArmorDTO1).isNotEqualTo(xaracterEquippedArmorDTO2);
        xaracterEquippedArmorDTO2.setId(xaracterEquippedArmorDTO1.getId());
        assertThat(xaracterEquippedArmorDTO1).isEqualTo(xaracterEquippedArmorDTO2);
        xaracterEquippedArmorDTO2.setId(2L);
        assertThat(xaracterEquippedArmorDTO1).isNotEqualTo(xaracterEquippedArmorDTO2);
        xaracterEquippedArmorDTO1.setId(null);
        assertThat(xaracterEquippedArmorDTO1).isNotEqualTo(xaracterEquippedArmorDTO2);
    }
}
