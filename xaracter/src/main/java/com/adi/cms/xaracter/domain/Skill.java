package com.adi.cms.xaracter.domain;

import com.adi.cms.xaracter.domain.enumeration.Difficulty;
import com.adi.cms.xaracter.domain.enumeration.SkillName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Skill.
 */
@Entity
@Table(name = "skill")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Skill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private SkillName name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", nullable = false)
    private Difficulty difficulty;

    @OneToMany(mappedBy = "skillId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "xaracterId", "skillId" }, allowSetters = true)
    private Set<XaracterSkill> xaracterSkills = new HashSet<>();

    @OneToMany(mappedBy = "skillId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "skillId" }, allowSetters = true)
    private Set<DefaultSkillOrAtribute> defaultSkillOrAtributes = new HashSet<>();

    @OneToMany(mappedBy = "skillId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "skillId" }, allowSetters = true)
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

    public Set<XaracterSkill> getXaracterSkills() {
        return this.xaracterSkills;
    }

    public void setXaracterSkills(Set<XaracterSkill> xaracterSkills) {
        if (this.xaracterSkills != null) {
            this.xaracterSkills.forEach(i -> i.setSkillId(null));
        }
        if (xaracterSkills != null) {
            xaracterSkills.forEach(i -> i.setSkillId(this));
        }
        this.xaracterSkills = xaracterSkills;
    }

    public Skill xaracterSkills(Set<XaracterSkill> xaracterSkills) {
        this.setXaracterSkills(xaracterSkills);
        return this;
    }

    public Skill addXaracterSkill(XaracterSkill xaracterSkill) {
        this.xaracterSkills.add(xaracterSkill);
        xaracterSkill.setSkillId(this);
        return this;
    }

    public Skill removeXaracterSkill(XaracterSkill xaracterSkill) {
        this.xaracterSkills.remove(xaracterSkill);
        xaracterSkill.setSkillId(null);
        return this;
    }

    public Set<DefaultSkillOrAtribute> getDefaultSkillOrAtributes() {
        return this.defaultSkillOrAtributes;
    }

    public void setDefaultSkillOrAtributes(Set<DefaultSkillOrAtribute> defaultSkillOrAtributes) {
        if (this.defaultSkillOrAtributes != null) {
            this.defaultSkillOrAtributes.forEach(i -> i.setSkillId(null));
        }
        if (defaultSkillOrAtributes != null) {
            defaultSkillOrAtributes.forEach(i -> i.setSkillId(this));
        }
        this.defaultSkillOrAtributes = defaultSkillOrAtributes;
    }

    public Skill defaultSkillOrAtributes(Set<DefaultSkillOrAtribute> defaultSkillOrAtributes) {
        this.setDefaultSkillOrAtributes(defaultSkillOrAtributes);
        return this;
    }

    public Skill addDefaultSkillOrAtribute(DefaultSkillOrAtribute defaultSkillOrAtribute) {
        this.defaultSkillOrAtributes.add(defaultSkillOrAtribute);
        defaultSkillOrAtribute.setSkillId(this);
        return this;
    }

    public Skill removeDefaultSkillOrAtribute(DefaultSkillOrAtribute defaultSkillOrAtribute) {
        this.defaultSkillOrAtributes.remove(defaultSkillOrAtribute);
        defaultSkillOrAtribute.setSkillId(null);
        return this;
    }

    public Set<PrereqSkillOrAtribute> getPrereqSkillOrAtributes() {
        return this.prereqSkillOrAtributes;
    }

    public void setPrereqSkillOrAtributes(Set<PrereqSkillOrAtribute> prereqSkillOrAtributes) {
        if (this.prereqSkillOrAtributes != null) {
            this.prereqSkillOrAtributes.forEach(i -> i.setSkillId(null));
        }
        if (prereqSkillOrAtributes != null) {
            prereqSkillOrAtributes.forEach(i -> i.setSkillId(this));
        }
        this.prereqSkillOrAtributes = prereqSkillOrAtributes;
    }

    public Skill prereqSkillOrAtributes(Set<PrereqSkillOrAtribute> prereqSkillOrAtributes) {
        this.setPrereqSkillOrAtributes(prereqSkillOrAtributes);
        return this;
    }

    public Skill addPrereqSkillOrAtribute(PrereqSkillOrAtribute prereqSkillOrAtribute) {
        this.prereqSkillOrAtributes.add(prereqSkillOrAtribute);
        prereqSkillOrAtribute.setSkillId(this);
        return this;
    }

    public Skill removePrereqSkillOrAtribute(PrereqSkillOrAtribute prereqSkillOrAtribute) {
        this.prereqSkillOrAtributes.remove(prereqSkillOrAtribute);
        prereqSkillOrAtribute.setSkillId(null);
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
