package com.adi.cms.character.service.mapper;

import com.adi.cms.character.domain.PrereqSkillOrAtribute;
import com.adi.cms.character.service.dto.PrereqSkillOrAtributeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PrereqSkillOrAtribute} and its DTO {@link PrereqSkillOrAtributeDTO}.
 */
@Mapper(componentModel = "spring", uses = { SkillMapper.class })
public interface PrereqSkillOrAtributeMapper extends EntityMapper<PrereqSkillOrAtributeDTO, PrereqSkillOrAtribute> {
    @Mapping(target = "skillId", source = "skillId", qualifiedByName = "id")
    PrereqSkillOrAtributeDTO toDto(PrereqSkillOrAtribute s);
}
