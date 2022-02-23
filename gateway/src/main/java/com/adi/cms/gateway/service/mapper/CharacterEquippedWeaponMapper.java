package com.adi.cms.gateway.service.mapper;

import com.adi.cms.gateway.domain.CharacterEquippedWeapon;
import com.adi.cms.gateway.service.dto.CharacterEquippedWeaponDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CharacterEquippedWeapon} and its DTO {@link CharacterEquippedWeaponDTO}.
 */
@Mapper(componentModel = "spring", uses = { WeaponMapper.class })
public interface CharacterEquippedWeaponMapper extends EntityMapper<CharacterEquippedWeaponDTO, CharacterEquippedWeapon> {
    @Mapping(target = "weapon", source = "weapon", qualifiedByName = "name")
    CharacterEquippedWeaponDTO toDto(CharacterEquippedWeapon s);
}
