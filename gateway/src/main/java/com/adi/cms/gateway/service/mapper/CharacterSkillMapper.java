package com.adi.cms.gateway.service.mapper;

import com.adi.cms.gateway.domain.CharacterSkill;
import com.adi.cms.gateway.service.dto.CharacterSkillDTO;
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
