package com.adi.cms.gateway.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class XaracterSkillMapperTest {

    private XaracterSkillMapper xaracterSkillMapper;

    @BeforeEach
    public void setUp() {
        xaracterSkillMapper = new XaracterSkillMapperImpl();
    }
}
