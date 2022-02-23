package com.adi.cms.character.service.mapper;

import com.adi.cms.character.domain.CharacterAttribute;
import com.adi.cms.character.service.dto.CharacterAttributeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CharacterAttribute} and its DTO {@link CharacterAttributeDTO}.
 */
@Mapper(componentModel = "spring", uses = { CharacterMapper.class })
public interface CharacterAttributeMapper extends EntityMapper<CharacterAttributeDTO, CharacterAttribute> {
    @Mapping(target = "characterId", source = "characterId", qualifiedByName = "id")
    CharacterAttributeDTO toDto(CharacterAttribute s);
}
