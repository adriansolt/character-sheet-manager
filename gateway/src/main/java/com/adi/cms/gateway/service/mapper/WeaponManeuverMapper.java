package com.adi.cms.gateway.service.mapper;

import com.adi.cms.gateway.domain.WeaponManeuver;
import com.adi.cms.gateway.service.dto.WeaponManeuverDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WeaponManeuver} and its DTO {@link WeaponManeuverDTO}.
 */
@Mapper(componentModel = "spring", uses = { WeaponMapper.class, ManeuverMapper.class })
public interface WeaponManeuverMapper extends EntityMapper<WeaponManeuverDTO, WeaponManeuver> {
    @Mapping(target = "weapon", source = "weapon", qualifiedByName = "name")
    @Mapping(target = "maneuver", source = "maneuver", qualifiedByName = "name")
    WeaponManeuverDTO toDto(WeaponManeuver s);
}
