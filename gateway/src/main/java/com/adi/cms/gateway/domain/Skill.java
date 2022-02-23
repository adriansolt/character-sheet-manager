package com.adi.cms.gateway.domain;

import com.adi.cms.gateway.domain.enumeration.Difficulty;
import com.adi.cms.gateway.domain.enumeration.SkillName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Skill.
 */
@Table("skill")
public class Skill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private SkillName name;

    @NotNull(message = "must not be null")
    @Column("difficulty")
    private Difficulty difficulty;

    @Transient
    @JsonIgnoreProperties(value = { "character", "skill" }, allowSetters = true)
    private Set<CharacterSkill> characterSkills = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "skill" }, allowSetters = true)
    private Set<DefaultSkillOrAtribute> defaultSkillOrAtributes = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "skill" }, allowSetters = true)
    private Set<PrereqSkillOrAtribute> prereqSkillOrAtributes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Skill id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SkillName getName() {
        return this.name;
    }

    public Skill name(SkillName name) {
        this.setName(name);
        return this;
    }

    public void setName(SkillName name) {
        this.name = name;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public Skill difficulty(Difficulty difficulty) {
        this.setDifficulty(difficulty);
        return this;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Set<CharacterSkill> getCharacterSkills() {
        return this.characterSkills;
    }

    public void setCharacterSkills(Set<CharacterSkill> characterSkills) {
        if (this.characterSkills != null) {
            this.characterSkills.forEach(i -> i.setSkill(null));
        }
        if (characterSkills != null) {
            characterSkills.forEach(i -> i.setSkill(this));
        }
        this.characterSkills = characterSkills;
    }

    public Skill characterSkills(Set<CharacterSkill> characterSkills) {
        this.setCharacterSkills(characterSkills);
        return this;
    }

    public Skill addCharacterSkill(CharacterSkill characterSkill) {
        this.characterSkills.add(characterSkill);
        characterSkill.setSkill(this);
        return this;
    }

    public Skill removeCharacterSkill(CharacterSkill characterSkill) {
        this.characterSkills.remove(characterSkill);
        characterSkill.setSkill(null);
        return this;
    }

    public Set<DefaultSkillOrAtribute> getDefaultSkillOrAtributes() {
        return this.defaultSkillOrAtributes;
    }

    public void setDefaultSkillOrAtributes(Set<DefaultSkillOrAtribute> defaultSkillOrAtributes) {
        if (this.defaultSkillOrAtributes != null) {
            this.defaultSkillOrAtributes.forEach(i -> i.setSkill(null));
        }
        if (defaultSkillOrAtributes != null) {
            defaultSkillOrAtributes.forEach(i -> i.setSkill(this));
        }
        this.defaultSkillOrAtributes = defaultSkillOrAtributes;
    }

    public Skill defaultSkillOrAtributes(Set<DefaultSkillOrAtribute> defaultSkillOrAtributes) {
        this.setDefaultSkillOrAtributes(defaultSkillOrAtributes);
        return this;
    }

    public Skill addDefaultSkillOrAtribute(DefaultSkillOrAtribute defaultSkillOrAtribute) {
        this.defaultSkillOrAtributes.add(defaultSkillOrAtribute);
        defaultSkillOrAtribute.setSkill(this);
        return this;
    }

    public Skill removeDefaultSkillOrAtribute(DefaultSkillOrAtribute defaultSkillOrAtribute) {
        this.defaultSkillOrAtributes.remove(defaultSkillOrAtribute);
        defaultSkillOrAtribute.setSkill(null);
        return this;
    }

    public Set<PrereqSkillOrAtribute> getPrereqSkillOrAtributes() {
        return this.prereqSkillOrAtributes;
    }

    public void setPrereqSkillOrAtributes(Set<PrereqSkillOrAtribute> prereqSkillOrAtributes) {
        if (this.prereqSkillOrAtributes != null) {
            this.prereqSkillOrAtributes.forEach(i -> i.setSkill(null));
        }
        if (prereqSkillOrAtributes != null) {
            prereqSkillOrAtributes.forEach(i -> i.setSkill(this));
        }
        this.prereqSkillOrAtributes = prereqSkillOrAtributes;
    }

    public Skill prereqSkillOrAtributes(Set<PrereqSkillOrAtribute> prereqSkillOrAtributes) {
        this.setPrereqSkillOrAtributes(prereqSkillOrAtributes);
        return this;
    }

    public Skill addPrereqSkillOrAtribute(PrereqSkillOrAtribute prereqSkillOrAtribute) {
        this.prereqSkillOrAtributes.add(prereqSkillOrAtribute);
        prereqSkillOrAtribute.setSkill(this);
        return this;
    }

    public Skill removePrereqSkillOrAtribute(PrereqSkillOrAtribute prereqSkillOrAtribute) {
        this.prereqSkillOrAtributes.remove(prereqSkillOrAtribute);
        prereqSkillOrAtribute.setSkill(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Skill)) {
            return false;
        }
        return id != null && id.equals(((Skill) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Skill{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", difficulty='" + getDifficulty() + "'" +
            "}";
    }
}
