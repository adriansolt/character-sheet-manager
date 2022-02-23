package com.adi.cms.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArmorPieceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArmorPiece.class);
        ArmorPiece armorPiece1 = new ArmorPiece();
        armorPiece1.setId(1L);
        ArmorPiece armorPiece2 = new ArmorPiece();
        armorPiece2.setId(armorPiece1.getId());
        assertThat(armorPiece1).isEqualTo(armorPiece2);
        armorPiece2.setId(2L);
        assertThat(armorPiece1).isNotEqualTo(armorPiece2);
        armorPiece1.setId(null);
        assertThat(armorPiece1).isNotEqualTo(armorPiece2);
    }
}
