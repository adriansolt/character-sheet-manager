package com.adi.cms.gateway.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WeaponManeuverMapperTest {

    private WeaponManeuverMapper weaponManeuverMapper;

    @BeforeEach
    public void setUp() {
        weaponManeuverMapper = new WeaponManeuverMapperImpl();
    }
}
