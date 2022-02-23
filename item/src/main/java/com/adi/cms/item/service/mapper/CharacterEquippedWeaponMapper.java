package com.adi.cms.item.service.mapper;

import com.adi.cms.item.domain.CharacterEquippedWeapon;
import com.adi.cms.item.service.dto.CharacterEquippedWeaponDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CharacterEquippedWeapon} and its DTO {@link CharacterEquippedWeaponDTO}.
 */
@Mapper(componentModel = "spring", uses = { WeaponMapper.class })
public interface CharacterEquippedWeaponMapper extends EntityMapper<CharacterEquippedWeaponDTO, CharacterEquippedWeapon> {
    @Mapping(target = "weapon", source = "weapon", qualifiedByName = "name")
    CharacterEquippedWeaponDTO toDto(CharacterEquippedWeapon s);
}
