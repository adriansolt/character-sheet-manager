package com.adi.cms.gateway.service.mapper;

import com.adi.cms.gateway.domain.CharacterEquippedArmor;
import com.adi.cms.gateway.service.dto.CharacterEquippedArmorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CharacterEquippedArmor} and its DTO {@link CharacterEquippedArmorDTO}.
 */
@Mapper(componentModel = "spring", uses = { ArmorPieceMapper.class })
public interface CharacterEquippedArmorMapper extends EntityMapper<CharacterEquippedArmorDTO, CharacterEquippedArmor> {
    @Mapping(target = "armorPiece", source = "armorPiece", qualifiedByName = "id")
    CharacterEquippedArmorDTO toDto(CharacterEquippedArmor s);
}
