package com.adi.cms.item.service.mapper;

import com.adi.cms.item.domain.Weapon;
import com.adi.cms.item.service.dto.WeaponDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Weapon} and its DTO {@link WeaponDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WeaponMapper extends EntityMapper<WeaponDTO, Weapon> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WeaponDTO toDtoId(Weapon weapon);
}
