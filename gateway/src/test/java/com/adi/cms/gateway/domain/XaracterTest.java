package com.adi.cms.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class XaracterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Xaracter.class);
        Xaracter xaracter1 = new Xaracter();
        xaracter1.setId(1L);
        Xaracter xaracter2 = new Xaracter();
        xaracter2.setId(xaracter1.getId());
        assertThat(xaracter1).isEqualTo(xaracter2);
        xaracter2.setId(2L);
        assertThat(xaracter1).isNotEqualTo(xaracter2);
        xaracter1.setId(null);
        assertThat(xaracter1).isNotEqualTo(xaracter2);
    }
}
