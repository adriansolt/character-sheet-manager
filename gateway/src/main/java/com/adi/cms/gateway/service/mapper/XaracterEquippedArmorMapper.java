package com.adi.cms.gateway.service.mapper;

import com.adi.cms.gateway.domain.XaracterEquippedArmor;
import com.adi.cms.gateway.service.dto.XaracterEquippedArmorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link XaracterEquippedArmor} and its DTO {@link XaracterEquippedArmorDTO}.
 */
@Mapper(componentModel = "spring", uses = { ArmorPieceMapper.class })
public interface XaracterEquippedArmorMapper extends EntityMapper<XaracterEquippedArmorDTO, XaracterEquippedArmor> {
    @Mapping(target = "armorPiece", source = "armorPiece", qualifiedByName = "id")
    XaracterEquippedArmorDTO toDto(XaracterEquippedArmor s);
}
