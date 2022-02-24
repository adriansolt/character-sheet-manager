package com.adi.cms.item.service.mapper;

import com.adi.cms.item.domain.CharacterEquippedArmor;
import com.adi.cms.item.service.dto.CharacterEquippedArmorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CharacterEquippedArmor} and its DTO {@link CharacterEquippedArmorDTO}.
 */
@Mapper(componentModel = "spring", uses = { ArmorPieceMapper.class })
public interface CharacterEquippedArmorMapper extends EntityMapper<CharacterEquippedArmorDTO, CharacterEquippedArmor> {
    @Mapping(target = "armorPiece", source = "armorPiece", qualifiedByName = "name")
    CharacterEquippedArmorDTO toDto(CharacterEquippedArmor s);
}
