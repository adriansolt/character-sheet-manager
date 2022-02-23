package com.adi.cms.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class XaracterAttributeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(XaracterAttribute.class);
        XaracterAttribute xaracterAttribute1 = new XaracterAttribute();
        xaracterAttribute1.setId(1L);
        XaracterAttribute xaracterAttribute2 = new XaracterAttribute();
        xaracterAttribute2.setId(xaracterAttribute1.getId());
        assertThat(xaracterAttribute1).isEqualTo(xaracterAttribute2);
        xaracterAttribute2.setId(2L);
        assertThat(xaracterAttribute1).isNotEqualTo(xaracterAttribute2);
        xaracterAttribute1.setId(null);
        assertThat(xaracterAttribute1).isNotEqualTo(xaracterAttribute2);
    }
}
