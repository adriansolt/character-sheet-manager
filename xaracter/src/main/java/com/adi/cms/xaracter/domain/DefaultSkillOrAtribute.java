package com.adi.cms.xaracter.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DefaultSkillOrAtribute.
 */
@Entity
@Table(name = "default_skill_or_atribute")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DefaultSkillOrAtribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "modifier", nullable = false)
    private Integer modifier;

    @ManyToOne
    @JsonIgnoreProperties(value = { "xaracterSkills", "defaultSkillOrAtributes", "prereqSkillOrAtributes" }, allowSetters = true)
    private Skill skillId;

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
    }

    public DefaultSkillOrAtribute skillId(Skill skill) {
        this.setSkillId(skill);
        return this;
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
