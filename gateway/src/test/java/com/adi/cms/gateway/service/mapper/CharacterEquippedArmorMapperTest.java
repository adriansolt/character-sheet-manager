package com.adi.cms.gateway.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CharacterEquippedArmorMapperTest {

    private CharacterEquippedArmorMapper characterEquippedArmorMapper;

    @BeforeEach
    public void setUp() {
        characterEquippedArmorMapper = new CharacterEquippedArmorMapperImpl();
    }
}
