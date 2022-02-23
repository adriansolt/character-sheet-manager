package com.adi.cms.item.service.mapper;

import com.adi.cms.item.domain.XaracterEquippedWeapon;
import com.adi.cms.item.service.dto.XaracterEquippedWeaponDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link XaracterEquippedWeapon} and its DTO {@link XaracterEquippedWeaponDTO}.
 */
@Mapper(componentModel = "spring", uses = { WeaponMapper.class })
public interface XaracterEquippedWeaponMapper extends EntityMapper<XaracterEquippedWeaponDTO, XaracterEquippedWeapon> {
    @Mapping(target = "weaponId", source = "weaponId", qualifiedByName = "name")
    XaracterEquippedWeaponDTO toDto(XaracterEquippedWeapon s);
}
