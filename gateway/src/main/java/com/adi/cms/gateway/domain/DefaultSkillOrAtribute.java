package com.adi.cms.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A DefaultSkillOrAtribute.
 */
@Table("default_skill_or_atribute")
public class DefaultSkillOrAtribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Column("modifier")
    private Integer modifier;

    @Transient
    @JsonIgnoreProperties(value = { "xaracterSkills", "defaultSkillOrAtributes", "prereqSkillOrAtributes" }, allowSetters = true)
    private Skill skillId;

    @Column("skill_id_id")
    private Long skillIdId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DefaultSkillOrAtribute id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public DefaultSkillOrAtribute name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getModifier() {
        return this.modifier;
    }

    public DefaultSkillOrAtribute modifier(Integer modifier) {
        this.setModifier(modifier);
        return this;
    }

    public void setModifier(Integer modifier) {
        this.modifier = modifier;
    }

    public Skill getSkillId() {
        return this.skillId;
    }

    public void setSkillId(Skill skill) {
        this.skillId = skill;
        this.skillIdId = skill != null ? skill.getId() : null;
    }

    public DefaultSkillOrAtribute skillId(Skill skill) {
        this.setSkillId(skill);
        return this;
    }

    public Long getSkillIdId() {
        return this.skillIdId;
    }

    public void setSkillIdId(Long skill) {
        this.skillIdId = skill;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultSkillOrAtribute)) {
            return false;
        }
        return id != null && id.equals(((DefaultSkillOrAtribute) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DefaultSkillOrAtribute{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", modifier=" + getModifier() +
            "}";
    }
}
