package com.adi.cms.gateway.service.mapper;

import com.adi.cms.gateway.domain.Note;
import com.adi.cms.gateway.service.dto.NoteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Note} and its DTO {@link NoteDTO}.
 */
@Mapper(componentModel = "spring", uses = { CharacterMapper.class })
public interface NoteMapper extends EntityMapper<NoteDTO, Note> {
    @Mapping(target = "character", source = "character", qualifiedByName = "name")
    NoteDTO toDto(Note s);
}
