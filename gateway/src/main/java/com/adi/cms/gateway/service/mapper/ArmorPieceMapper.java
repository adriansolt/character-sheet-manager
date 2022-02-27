package com.adi.cms.gateway.service.mapper;

import com.adi.cms.gateway.domain.ArmorPiece;
import com.adi.cms.gateway.service.dto.ArmorPieceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArmorPiece} and its DTO {@link ArmorPieceDTO}.
 */
@Mapper(componentModel = "spring", uses = { CampaignMapper.class, CharacterMapper.class })
public interface ArmorPieceMapper extends EntityMapper<ArmorPieceDTO, ArmorPiece> {
    @Mapping(target = "campaign", source = "campaign", qualifiedByName = "name")
    @Mapping(target = "character", source = "character", qualifiedByName = "name")
    ArmorPieceDTO toDto(ArmorPiece s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ArmorPieceDTO toDtoName(ArmorPiece armorPiece);
}
