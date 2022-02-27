package com.adi.cms.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A CharacterSkill.
 */
@Table("character_skill")
public class CharacterSkill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("points")
    private Integer points;

    @NotNull(message = "must not be null")
    @Column("skill_modifier")
    private Integer skillModifier;

    @Transient
    @JsonIgnoreProperties(
        value = { "items", "weapons", "armorPieces", "notes", "characterAttributes", "characterSkills", "user", "campaign" },
        allowSetters = true
    )
    private Character character;

    @Transient
    @JsonIgnoreProperties(value = { "characterSkills", "defaultSkillOrAtributes", "prereqSkillOrAtributes" }, allowSetters = true)
    private Skill skill;

    @Column("character_id")
    private Long characterId;

    @Column("skill_id")
    private Long skillId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CharacterSkill id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPoints() {
        return this.points;
    }

    public CharacterSkill points(Integer points) {
        this.setPoints(points);
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getSkillModifier() {
        return this.skillModifier;
    }

    public CharacterSkill skillModifier(Integer skillModifier) {
        this.setSkillModifier(skillModifier);
        return this;
    }

    public void setSkillModifier(Integer skillModifier) {
        this.skillModifier = skillModifier;
    }

    public Character getCharacter() {
        return this.character;
    }

    public void setCharacter(Character character) {
        this.character = character;
        this.characterId = character != null ? character.getId() : null;
    }

    public CharacterSkill character(Character character) {
        this.setCharacter(character);
        return this;
    }

    public Skill getSkill() {
        return this.skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
        this.skillId = skill != null ? skill.getId() : null;
    }

    public CharacterSkill skill(Skill skill) {
        this.setSkill(skill);
        return this;
    }

    public Long getCharacterId() {
        return this.characterId;
    }

    public void setCharacterId(Long character) {
        this.characterId = character;
    }

    public Long getSkillId() {
        return this.skillId;
    }

    public void setSkillId(Long skill) {
        this.skillId = skill;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CharacterSkill)) {
            return false;
        }
        return id != null && id.equals(((CharacterSkill) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CharacterSkill{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            ", skillModifier=" + getSkillModifier() +
            "}";
    }
}
