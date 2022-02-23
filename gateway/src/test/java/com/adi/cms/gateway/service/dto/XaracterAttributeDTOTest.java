package com.adi.cms.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class XaracterAttributeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(XaracterAttributeDTO.class);
        XaracterAttributeDTO xaracterAttributeDTO1 = new XaracterAttributeDTO();
        xaracterAttributeDTO1.setId(1L);
        XaracterAttributeDTO xaracterAttributeDTO2 = new XaracterAttributeDTO();
        assertThat(xaracterAttributeDTO1).isNotEqualTo(xaracterAttributeDTO2);
        xaracterAttributeDTO2.setId(xaracterAttributeDTO1.getId());
        assertThat(xaracterAttributeDTO1).isEqualTo(xaracterAttributeDTO2);
        xaracterAttributeDTO2.setId(2L);
        assertThat(xaracterAttributeDTO1).isNotEqualTo(xaracterAttributeDTO2);
        xaracterAttributeDTO1.setId(null);
        assertThat(xaracterAttributeDTO1).isNotEqualTo(xaracterAttributeDTO2);
    }
}
