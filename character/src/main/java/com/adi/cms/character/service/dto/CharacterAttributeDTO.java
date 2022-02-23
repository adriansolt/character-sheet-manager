package com.adi.cms.character.service.dto;

import com.adi.cms.character.domain.enumeration.AttributeName;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.adi.cms.character.domain.CharacterAttribute} entity.
 */
public class CharacterAttributeDTO implements Serializable {

    private Long id;

    @NotNull
    private AttributeName name;

    @NotNull
    private Integer points;

    private Integer attributeModifier;

    private CharacterDTO characterId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AttributeName getName() {
        return name;
    }

    public void setName(AttributeName name) {
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getAttributeModifier() {
        return attributeModifier;
    }

    public void setAttributeModifier(Integer attributeModifier) {
        this.attributeModifier = attributeModifier;
    }

    public CharacterDTO getCharacterId() {
        return characterId;
    }

    public void setCharacterId(CharacterDTO characterId) {
        this.characterId = characterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CharacterAttributeDTO)) {
            return false;
        }

        CharacterAttributeDTO characterAttributeDTO = (CharacterAttributeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, characterAttributeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CharacterAttributeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", points=" + getPoints() +
            ", attributeModifier=" + getAttributeModifier() +
            ", characterId=" + getCharacterId() +
            "}";
    }
}
