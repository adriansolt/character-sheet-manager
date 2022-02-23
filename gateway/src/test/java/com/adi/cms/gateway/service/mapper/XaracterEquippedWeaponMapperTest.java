package com.adi.cms.gateway.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class XaracterEquippedWeaponMapperTest {

    private XaracterEquippedWeaponMapper xaracterEquippedWeaponMapper;

    @BeforeEach
    public void setUp() {
        xaracterEquippedWeaponMapper = new XaracterEquippedWeaponMapperImpl();
    }
}
