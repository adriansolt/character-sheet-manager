package com.adi.cms.character.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CharacterSkill.
 */
@Entity
@Table(name = "character_skill")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CharacterSkill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "skill_modifier")
    private Integer skillModifier;

    @ManyToOne
    @JsonIgnoreProperties(value = { "notes", "characterAttributes", "characterSkills", "user" }, allowSetters = true)
    private Character characterId;

    @ManyToOne
    @JsonIgnoreProperties(value = { "characterSkills", "defaultSkillOrAtributes", "prereqSkillOrAtributes" }, allowSetters = true)
    private Skill skillId;

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

    public Character getCharacterId() {
        return this.characterId;
    }

    public void setCharacterId(Character character) {
        this.characterId = character;
    }

    public CharacterSkill characterId(Character character) {
        this.setCharacterId(character);
        return this;
    }

    public Skill getSkillId() {
        return this.skillId;
    }

    public void setSkillId(Skill skill) {
        this.skillId = skill;
    }

    public CharacterSkill skillId(Skill skill) {
        this.setSkillId(skill);
        return this;
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
