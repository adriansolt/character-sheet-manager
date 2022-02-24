package com.adi.cms.item.service.mapper;

import com.adi.cms.item.domain.ArmorPiece;
import com.adi.cms.item.service.dto.ArmorPieceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArmorPiece} and its DTO {@link ArmorPieceDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ArmorPieceMapper extends EntityMapper<ArmorPieceDTO, ArmorPiece> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ArmorPieceDTO toDtoName(ArmorPiece armorPiece);
}
