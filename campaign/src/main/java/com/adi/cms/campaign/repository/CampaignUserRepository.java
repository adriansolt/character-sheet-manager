package com.adi.cms.campaign.repository;

import com.adi.cms.campaign.domain.CampaignUser;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CampaignUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CampaignUserRepository extends JpaRepository<CampaignUser, Long> {
    @Query("select campaignUser from CampaignUser campaignUser where campaignUser.user.login = ?#{principal.preferredUsername}")
    List<CampaignUser> findByUserIsCurrentUser();
}
