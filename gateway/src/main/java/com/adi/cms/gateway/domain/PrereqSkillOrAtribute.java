package com.adi.cms.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PrereqSkillOrAtribute.
 */
@Table("prereq_skill_or_atribute")
public class PrereqSkillOrAtribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Column("level")
    private Integer level;

    @Transient
    @JsonIgnoreProperties(value = { "characterSkills", "defaultSkillOrAtributes", "prereqSkillOrAtributes" }, allowSetters = true)
    private Skill skill;

    @Column("skill_id")
    private Long skillId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PrereqSkillOrAtribute id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PrereqSkillOrAtribute name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return this.level;
    }

    public PrereqSkillOrAtribute level(Integer level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Skill getSkill() {
        return this.skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
        this.skillId = skill != null ? skill.getId() : null;
    }

    public PrereqSkillOrAtribute skill(Skill skill) {
        this.setSkill(skill);
        return this;
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
        if (!(o instanceof PrereqSkillOrAtribute)) {
            return false;
        }
        return id != null && id.equals(((PrereqSkillOrAtribute) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrereqSkillOrAtribute{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", level=" + getLevel() +
            "}";
    }
}
