package com.adi.cms.character.service.mapper;

import com.adi.cms.character.domain.Skill;
import com.adi.cms.character.service.dto.SkillDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Skill} and its DTO {@link SkillDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SkillMapper extends EntityMapper<SkillDTO, Skill> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SkillDTO toDtoId(Skill skill);
}
