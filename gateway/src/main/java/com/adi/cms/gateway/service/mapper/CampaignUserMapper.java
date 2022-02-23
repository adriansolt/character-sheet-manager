package com.adi.cms.gateway.service.mapper;

import com.adi.cms.gateway.domain.CampaignUser;
import com.adi.cms.gateway.service.dto.CampaignUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CampaignUser} and its DTO {@link CampaignUserDTO}.
 */
@Mapper(componentModel = "spring", uses = { CampaignMapper.class, UserMapper.class })
public interface CampaignUserMapper extends EntityMapper<CampaignUserDTO, CampaignUser> {
    @Mapping(target = "campaignId", source = "campaignId", qualifiedByName = "id")
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    CampaignUserDTO toDto(CampaignUser s);
}
