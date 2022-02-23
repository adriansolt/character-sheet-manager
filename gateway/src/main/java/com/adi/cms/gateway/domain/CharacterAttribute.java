package com.adi.cms.gateway.domain;

import com.adi.cms.gateway.domain.enumeration.AttributeName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A CharacterAttribute.
 */
@Table("character_attribute")
public class CharacterAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private AttributeName name;

    @NotNull(message = "must not be null")
    @Column("points")
    private Integer points;

    @Column("attribute_modifier")
    private Integer attributeModifier;

    @Transient
    @JsonIgnoreProperties(value = { "notes", "characterAttributes", "characterSkills", "user" }, allowSetters = true)
    private Character character;

    @Column("character_id")
    private Long characterId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CharacterAttribute id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AttributeName getName() {
        return this.name;
    }

    public CharacterAttribute name(AttributeName name) {
        this.setName(name);
        return this;
    }

    public void setName(AttributeName name) {
        this.name = name;
    }

    public Integer getPoints() {
        return this.points;
    }

    public CharacterAttribute points(Integer points) {
        this.setPoints(points);
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getAttributeModifier() {
        return this.attributeModifier;
    }

    public CharacterAttribute attributeModifier(Integer attributeModifier) {
        this.setAttributeModifier(attributeModifier);
        return this;
    }

    public void setAttributeModifier(Integer attributeModifier) {
        this.attributeModifier = attributeModifier;
    }

    public Character getCharacter() {
        return this.character;
    }

    public void setCharacter(Character character) {
        this.character = character;
        this.characterId = character != null ? character.getId() : null;
    }

    public CharacterAttribute character(Character character) {
        this.setCharacter(character);
        return this;
    }

    public Long getCharacterId() {
        return this.characterId;
    }

    public void setCharacterId(Long character) {
        this.characterId = character;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CharacterAttribute)) {
            return false;
        }
        return id != null && id.equals(((CharacterAttribute) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CharacterAttribute{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", points=" + getPoints() +
            ", attributeModifier=" + getAttributeModifier() +
            "}";
    }
}
