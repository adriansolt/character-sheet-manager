package com.adi.cms.gateway.service.mapper;

import com.adi.cms.gateway.domain.Item;
import com.adi.cms.gateway.service.dto.ItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Item} and its DTO {@link ItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { CampaignMapper.class, CharacterMapper.class })
public interface ItemMapper extends EntityMapper<ItemDTO, Item> {
    @Mapping(target = "campaign", source = "campaign", qualifiedByName = "name")
    @Mapping(target = "character", source = "character", qualifiedByName = "name")
    ItemDTO toDto(Item s);
}
