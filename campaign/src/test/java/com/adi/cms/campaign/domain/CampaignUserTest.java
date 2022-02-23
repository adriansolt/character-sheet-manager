package com.adi.cms.campaign.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.adi.cms.campaign.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CampaignUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CampaignUser.class);
        CampaignUser campaignUser1 = new CampaignUser();
        campaignUser1.setId(1L);
        CampaignUser campaignUser2 = new CampaignUser();
        campaignUser2.setId(campaignUser1.getId());
        assertThat(campaignUser1).isEqualTo(campaignUser2);
        campaignUser2.setId(2L);
        assertThat(campaignUser1).isNotEqualTo(campaignUser2);
        campaignUser1.setId(null);
        assertThat(campaignUser1).isNotEqualTo(campaignUser2);
    }
}
