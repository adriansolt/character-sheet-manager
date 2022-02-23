package com.adi.cms.campaign.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CampaignUserMapperTest {

    private CampaignUserMapper campaignUserMapper;

    @BeforeEach
    public void setUp() {
        campaignUserMapper = new CampaignUserMapperImpl();
    }
}
