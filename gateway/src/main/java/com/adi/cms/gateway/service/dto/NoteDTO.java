package com.adi.cms.gateway.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.adi.cms.gateway.domain.Note} entity.
 */
public class NoteDTO implements Serializable {

    private Long id;

    private String description;

    private XaracterDTO xaracterId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public XaracterDTO getXaracterId() {
        return xaracterId;
    }

    public void setXaracterId(XaracterDTO xaracterId) {
        this.xaracterId = xaracterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NoteDTO)) {
            return false;
        }

        NoteDTO noteDTO = (NoteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, noteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NoteDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", xaracterId=" + getXaracterId() +
            "}";
    }
}
