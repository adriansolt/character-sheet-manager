package com.adi.cms.xaracter.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.xaracter.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class XaracterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(XaracterDTO.class);
        XaracterDTO xaracterDTO1 = new XaracterDTO();
        xaracterDTO1.setId(1L);
        XaracterDTO xaracterDTO2 = new XaracterDTO();
        assertThat(xaracterDTO1).isNotEqualTo(xaracterDTO2);
        xaracterDTO2.setId(xaracterDTO1.getId());
        assertThat(xaracterDTO1).isEqualTo(xaracterDTO2);
        xaracterDTO2.setId(2L);
        assertThat(xaracterDTO1).isNotEqualTo(xaracterDTO2);
        xaracterDTO1.setId(null);
        assertThat(xaracterDTO1).isNotEqualTo(xaracterDTO2);
    }
}
