package com.adi.cms.character.service.mapper;

import com.adi.cms.character.domain.Note;
import com.adi.cms.character.service.dto.NoteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Note} and its DTO {@link NoteDTO}.
 */
@Mapper(componentModel = "spring", uses = { CharacterMapper.class })
public interface NoteMapper extends EntityMapper<NoteDTO, Note> {
    @Mapping(target = "character", source = "character", qualifiedByName = "name")
    NoteDTO toDto(Note s);
}
