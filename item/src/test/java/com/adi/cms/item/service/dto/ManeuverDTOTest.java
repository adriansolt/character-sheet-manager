package com.adi.cms.item.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.item.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ManeuverDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManeuverDTO.class);
        ManeuverDTO maneuverDTO1 = new ManeuverDTO();
        maneuverDTO1.setId(1L);
        ManeuverDTO maneuverDTO2 = new ManeuverDTO();
        assertThat(maneuverDTO1).isNotEqualTo(maneuverDTO2);
        maneuverDTO2.setId(maneuverDTO1.getId());
        assertThat(maneuverDTO1).isEqualTo(maneuverDTO2);
        maneuverDTO2.setId(2L);
        assertThat(maneuverDTO1).isNotEqualTo(maneuverDTO2);
        maneuverDTO1.setId(null);
        assertThat(maneuverDTO1).isNotEqualTo(maneuverDTO2);
    }
}
