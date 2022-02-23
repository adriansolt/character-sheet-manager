package com.adi.cms.item.service.mapper;

import com.adi.cms.item.domain.WeaponManeuver;
import com.adi.cms.item.service.dto.WeaponManeuverDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WeaponManeuver} and its DTO {@link WeaponManeuverDTO}.
 */
@Mapper(componentModel = "spring", uses = { WeaponMapper.class, ManeuverMapper.class })
public interface WeaponManeuverMapper extends EntityMapper<WeaponManeuverDTO, WeaponManeuver> {
    @Mapping(target = "weaponId", source = "weaponId", qualifiedByName = "name")
    @Mapping(target = "maneuverId", source = "maneuverId", qualifiedByName = "name")
    WeaponManeuverDTO toDto(WeaponManeuver s);
}
