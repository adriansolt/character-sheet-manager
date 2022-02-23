package com.adi.cms.character.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CharacterMapperTest {

    private CharacterMapper characterMapper;

    @BeforeEach
    public void setUp() {
        characterMapper = new CharacterMapperImpl();
    }
}
