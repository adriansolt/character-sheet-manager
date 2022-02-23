package com.adi.cms.campaign.service.mapper;

import com.adi.cms.campaign.domain.Campaign;
import com.adi.cms.campaign.service.dto.CampaignDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Campaign} and its DTO {@link CampaignDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CampaignMapper extends EntityMapper<CampaignDTO, Campaign> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CampaignDTO toDtoId(Campaign campaign);
}
