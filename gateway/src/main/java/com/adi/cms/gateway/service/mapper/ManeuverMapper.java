package com.adi.cms.gateway.service.mapper;

import com.adi.cms.gateway.domain.Maneuver;
import com.adi.cms.gateway.service.dto.ManeuverDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Maneuver} and its DTO {@link ManeuverDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ManeuverMapper extends EntityMapper<ManeuverDTO, Maneuver> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ManeuverDTO toDtoId(Maneuver maneuver);
}
