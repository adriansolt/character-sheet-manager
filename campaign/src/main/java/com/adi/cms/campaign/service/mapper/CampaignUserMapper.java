package com.adi.cms.campaign.service.mapper;

import com.adi.cms.campaign.domain.CampaignUser;
import com.adi.cms.campaign.service.dto.CampaignUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CampaignUser} and its DTO {@link CampaignUserDTO}.
 */
@Mapper(componentModel = "spring", uses = { CampaignMapper.class, UserMapper.class })
public interface CampaignUserMapper extends EntityMapper<CampaignUserDTO, CampaignUser> {
    @Mapping(target = "campaign", source = "campaign", qualifiedByName = "name")
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    CampaignUserDTO toDto(CampaignUser s);
}
