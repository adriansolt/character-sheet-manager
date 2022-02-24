package com.adi.cms.gateway.service.mapper;

import com.adi.cms.gateway.domain.Weapon;
import com.adi.cms.gateway.service.dto.WeaponDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Weapon} and its DTO {@link WeaponDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WeaponMapper extends EntityMapper<WeaponDTO, Weapon> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    WeaponDTO toDtoName(Weapon weapon);
}
