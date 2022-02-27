package com.adi.cms.gateway.service.mapper;

import com.adi.cms.gateway.domain.Weapon;
import com.adi.cms.gateway.service.dto.WeaponDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Weapon} and its DTO {@link WeaponDTO}.
 */
@Mapper(componentModel = "spring", uses = { CampaignMapper.class, CharacterMapper.class })
public interface WeaponMapper extends EntityMapper<WeaponDTO, Weapon> {
    @Mapping(target = "campaign", source = "campaign", qualifiedByName = "name")
    @Mapping(target = "character", source = "character", qualifiedByName = "name")
    WeaponDTO toDto(Weapon s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    WeaponDTO toDtoName(Weapon weapon);
}
