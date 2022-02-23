package com.adi.cms.xaracter.service.mapper;

import com.adi.cms.xaracter.domain.Note;
import com.adi.cms.xaracter.service.dto.NoteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Note} and its DTO {@link NoteDTO}.
 */
@Mapper(componentModel = "spring", uses = { XaracterMapper.class })
public interface NoteMapper extends EntityMapper<NoteDTO, Note> {
    @Mapping(target = "xaracterId", source = "xaracterId", qualifiedByName = "id")
    NoteDTO toDto(Note s);
}
