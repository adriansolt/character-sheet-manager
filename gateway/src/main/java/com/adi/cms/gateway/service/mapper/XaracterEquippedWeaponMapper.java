package com.adi.cms.gateway.service.mapper;

import com.adi.cms.gateway.domain.XaracterEquippedWeapon;
import com.adi.cms.gateway.service.dto.XaracterEquippedWeaponDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link XaracterEquippedWeapon} and its DTO {@link XaracterEquippedWeaponDTO}.
 */
@Mapper(componentModel = "spring", uses = { WeaponMapper.class })
public interface XaracterEquippedWeaponMapper extends EntityMapper<XaracterEquippedWeaponDTO, XaracterEquippedWeapon> {
    @Mapping(target = "weaponId", source = "weaponId", qualifiedByName = "name")
    XaracterEquippedWeaponDTO toDto(XaracterEquippedWeapon s);
}
