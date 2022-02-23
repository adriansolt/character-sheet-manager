package com.adi.cms.item.service.mapper;

import com.adi.cms.item.domain.XaracterEquippedArmor;
import com.adi.cms.item.service.dto.XaracterEquippedArmorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link XaracterEquippedArmor} and its DTO {@link XaracterEquippedArmorDTO}.
 */
@Mapper(componentModel = "spring", uses = { ArmorPieceMapper.class })
public interface XaracterEquippedArmorMapper extends EntityMapper<XaracterEquippedArmorDTO, XaracterEquippedArmor> {
    @Mapping(target = "armorPiece", source = "armorPiece", qualifiedByName = "id")
    XaracterEquippedArmorDTO toDto(XaracterEquippedArmor s);
}
