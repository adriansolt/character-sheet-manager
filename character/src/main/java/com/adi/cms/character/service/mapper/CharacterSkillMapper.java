package com.adi.cms.character.service.mapper;

import com.adi.cms.character.domain.CharacterSkill;
import com.adi.cms.character.service.dto.CharacterSkillDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CharacterSkill} and its DTO {@link CharacterSkillDTO}.
 */
@Mapper(componentModel = "spring", uses = { CharacterMapper.class, SkillMapper.class })
public interface CharacterSkillMapper extends EntityMapper<CharacterSkillDTO, CharacterSkill> {
    @Mapping(target = "character", source = "character", qualifiedByName = "name")
    @Mapping(target = "skill", source = "skill", qualifiedByName = "name")
    CharacterSkillDTO toDto(CharacterSkill s);
}
