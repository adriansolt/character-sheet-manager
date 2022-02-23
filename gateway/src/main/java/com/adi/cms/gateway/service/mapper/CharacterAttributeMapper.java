package com.adi.cms.gateway.service.mapper;

import com.adi.cms.gateway.domain.CharacterAttribute;
import com.adi.cms.gateway.service.dto.CharacterAttributeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CharacterAttribute} and its DTO {@link CharacterAttributeDTO}.
 */
@Mapper(componentModel = "spring", uses = { CharacterMapper.class })
public interface CharacterAttributeMapper extends EntityMapper<CharacterAttributeDTO, CharacterAttribute> {
    @Mapping(target = "character", source = "character", qualifiedByName = "name")
    CharacterAttributeDTO toDto(CharacterAttribute s);
}
