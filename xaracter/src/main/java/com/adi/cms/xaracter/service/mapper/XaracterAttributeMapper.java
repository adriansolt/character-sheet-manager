package com.adi.cms.xaracter.service.mapper;

import com.adi.cms.xaracter.domain.XaracterAttribute;
import com.adi.cms.xaracter.service.dto.XaracterAttributeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link XaracterAttribute} and its DTO {@link XaracterAttributeDTO}.
 */
@Mapper(componentModel = "spring", uses = { XaracterMapper.class })
public interface XaracterAttributeMapper extends EntityMapper<XaracterAttributeDTO, XaracterAttribute> {
    @Mapping(target = "xaracterId", source = "xaracterId", qualifiedByName = "id")
    XaracterAttributeDTO toDto(XaracterAttribute s);
}
