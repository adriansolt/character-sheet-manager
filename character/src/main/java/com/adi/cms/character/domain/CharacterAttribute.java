package com.adi.cms.character.domain;

import com.adi.cms.character.domain.enumeration.AttributeName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CharacterAttribute.
 */
@Entity
@Table(name = "character_attribute")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CharacterAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private AttributeName name;

    @NotNull
    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "attribute_modifier")
    private Integer attributeModifier;

    @ManyToOne
    @JsonIgnoreProperties(value = { "notes", "characterAttributes", "characterSkills", "user" }, allowSetters = true)
    private Character character;

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
    }

    public CharacterAttribute character(Character character) {
        this.setCharacter(character);
        return this;
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
