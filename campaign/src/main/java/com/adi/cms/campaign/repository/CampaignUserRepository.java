package com.adi.cms.campaign.repository;

import com.adi.cms.campaign.domain.CampaignUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CampaignUser entity.
 */
@Repository
public interface CampaignUserRepository extends JpaRepository<CampaignUser, Long> {
    @Query("select campaignUser from CampaignUser campaignUser where campaignUser.user.login = ?#{principal.preferredUsername}")
    List<CampaignUser> findByUserIsCurrentUser();

    default Optional<CampaignUser> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CampaignUser> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CampaignUser> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct campaignUser from CampaignUser campaignUser left join fetch campaignUser.campaign left join fetch campaignUser.user",
        countQuery = "select count(distinct campaignUser) from CampaignUser campaignUser"
    )
    Page<CampaignUser> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct campaignUser from CampaignUser campaignUser left join fetch campaignUser.campaign left join fetch campaignUser.user"
    )
    List<CampaignUser> findAllWithToOneRelationships();

    @Query(
        "select campaignUser from CampaignUser campaignUser left join fetch campaignUser.campaign left join fetch campaignUser.user where campaignUser.id =:id"
    )
    Optional<CampaignUser> findOneWithToOneRelationships(@Param("id") Long id);
}
