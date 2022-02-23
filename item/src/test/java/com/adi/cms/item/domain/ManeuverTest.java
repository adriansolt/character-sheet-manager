package com.adi.cms.item.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.item.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ManeuverTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Maneuver.class);
        Maneuver maneuver1 = new Maneuver();
        maneuver1.setId(1L);
        Maneuver maneuver2 = new Maneuver();
        maneuver2.setId(maneuver1.getId());
        assertThat(maneuver1).isEqualTo(maneuver2);
        maneuver2.setId(2L);
        assertThat(maneuver1).isNotEqualTo(maneuver2);
        maneuver1.setId(null);
        assertThat(maneuver1).isNotEqualTo(maneuver2);
    }
}
