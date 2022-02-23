package com.adi.cms.character.service.mapper;

import com.adi.cms.character.domain.Character;
import com.adi.cms.character.service.dto.CharacterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Character} and its DTO {@link CharacterDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface CharacterMapper extends EntityMapper<CharacterDTO, Character> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    CharacterDTO toDto(Character s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CharacterDTO toDtoId(Character character);
}
