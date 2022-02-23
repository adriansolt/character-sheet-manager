package com.adi.cms.item.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArmorPieceMapperTest {

    private ArmorPieceMapper armorPieceMapper;

    @BeforeEach
    public void setUp() {
        armorPieceMapper = new ArmorPieceMapperImpl();
    }
}
