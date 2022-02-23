package com.adi.cms.campaign.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.campaign.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CampaignUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CampaignUserDTO.class);
        CampaignUserDTO campaignUserDTO1 = new CampaignUserDTO();
        campaignUserDTO1.setId(1L);
        CampaignUserDTO campaignUserDTO2 = new CampaignUserDTO();
        assertThat(campaignUserDTO1).isNotEqualTo(campaignUserDTO2);
        campaignUserDTO2.setId(campaignUserDTO1.getId());
        assertThat(campaignUserDTO1).isEqualTo(campaignUserDTO2);
        campaignUserDTO2.setId(2L);
        assertThat(campaignUserDTO1).isNotEqualTo(campaignUserDTO2);
        campaignUserDTO1.setId(null);
        assertThat(campaignUserDTO1).isNotEqualTo(campaignUserDTO2);
    }
}
