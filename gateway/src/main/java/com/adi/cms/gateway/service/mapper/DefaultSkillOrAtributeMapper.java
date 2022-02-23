package com.adi.cms.gateway.service.mapper;

import com.adi.cms.gateway.domain.DefaultSkillOrAtribute;
import com.adi.cms.gateway.service.dto.DefaultSkillOrAtributeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DefaultSkillOrAtribute} and its DTO {@link DefaultSkillOrAtributeDTO}.
 */
@Mapper(componentModel = "spring", uses = { SkillMapper.class })
public interface DefaultSkillOrAtributeMapper extends EntityMapper<DefaultSkillOrAtributeDTO, DefaultSkillOrAtribute> {
    @Mapping(target = "skill", source = "skill", qualifiedByName = "name")
    DefaultSkillOrAtributeDTO toDto(DefaultSkillOrAtribute s);
}
