package com.adi.cms.gateway.service.mapper;

import com.adi.cms.gateway.domain.Xaracter;
import com.adi.cms.gateway.service.dto.XaracterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Xaracter} and its DTO {@link XaracterDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface XaracterMapper extends EntityMapper<XaracterDTO, Xaracter> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    XaracterDTO toDto(Xaracter s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    XaracterDTO toDtoId(Xaracter xaracter);
}
