package com.adi.cms.xaracter.service.mapper;

import com.adi.cms.xaracter.domain.XaracterSkill;
import com.adi.cms.xaracter.service.dto.XaracterSkillDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link XaracterSkill} and its DTO {@link XaracterSkillDTO}.
 */
@Mapper(componentModel = "spring", uses = { XaracterMapper.class, SkillMapper.class })
public interface XaracterSkillMapper extends EntityMapper<XaracterSkillDTO, XaracterSkill> {
    @Mapping(target = "xaracterId", source = "xaracterId", qualifiedByName = "id")
    @Mapping(target = "skillId", source = "skillId", qualifiedByName = "id")
    XaracterSkillDTO toDto(XaracterSkill s);
}
