package com.adi.cms.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArmorPieceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArmorPieceDTO.class);
        ArmorPieceDTO armorPieceDTO1 = new ArmorPieceDTO();
        armorPieceDTO1.setId(1L);
        ArmorPieceDTO armorPieceDTO2 = new ArmorPieceDTO();
        assertThat(armorPieceDTO1).isNotEqualTo(armorPieceDTO2);
        armorPieceDTO2.setId(armorPieceDTO1.getId());
        assertThat(armorPieceDTO1).isEqualTo(armorPieceDTO2);
        armorPieceDTO2.setId(2L);
        assertThat(armorPieceDTO1).isNotEqualTo(armorPieceDTO2);
        armorPieceDTO1.setId(null);
        assertThat(armorPieceDTO1).isNotEqualTo(armorPieceDTO2);
    }
}
