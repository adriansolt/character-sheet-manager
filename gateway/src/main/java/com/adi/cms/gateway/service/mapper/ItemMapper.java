package com.adi.cms.gateway.service.mapper;

import com.adi.cms.gateway.domain.Item;
import com.adi.cms.gateway.service.dto.ItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Item} and its DTO {@link ItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { CharacterMapper.class })
public interface ItemMapper extends EntityMapper<ItemDTO, Item> {
    @Mapping(target = "character", source = "character", qualifiedByName = "name")
    ItemDTO toDto(Item s);
}
