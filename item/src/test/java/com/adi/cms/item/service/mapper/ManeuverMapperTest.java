package com.adi.cms.item.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ManeuverMapperTest {

    private ManeuverMapper maneuverMapper;

    @BeforeEach
    public void setUp() {
        maneuverMapper = new ManeuverMapperImpl();
    }
}
