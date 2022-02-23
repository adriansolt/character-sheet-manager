package com.adi.cms.gateway.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class XaracterAttributeMapperTest {

    private XaracterAttributeMapper xaracterAttributeMapper;

    @BeforeEach
    public void setUp() {
        xaracterAttributeMapper = new XaracterAttributeMapperImpl();
    }
}
