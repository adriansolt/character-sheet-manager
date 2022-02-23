package com.adi.cms.xaracter.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class XaracterMapperTest {

    private XaracterMapper xaracterMapper;

    @BeforeEach
    public void setUp() {
        xaracterMapper = new XaracterMapperImpl();
    }
}
