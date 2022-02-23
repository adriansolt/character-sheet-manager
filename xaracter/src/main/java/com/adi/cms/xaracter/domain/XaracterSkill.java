package com.adi.cms.xaracter.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A XaracterSkill.
 */
@Entity
@Table(name = "xaracter_skill")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class XaracterSkill implements Serializable {

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
    @JsonIgnoreProperties(value = { "notes", "xaracterAttributes", "xaracterSkills", "user" }, allowSetters = true)
    private Xaracter xaracterId;

    @ManyToOne
    @JsonIgnoreProperties(value = { "xaracterSkills", "defaultSkillOrAtributes", "prereqSkillOrAtributes" }, allowSetters = true)
    private Skill skillId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public XaracterSkill id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPoints() {
        return this.points;
    }

    public XaracterSkill points(Integer points) {
        this.setPoints(points);
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getSkillModifier() {
        return this.skillModifier;
    }

    public XaracterSkill skillModifier(Integer skillModifier) {
        this.setSkillModifier(skillModifier);
        return this;
    }

    public void setSkillModifier(Integer skillModifier) {
        this.skillModifier = skillModifier;
    }

    public Xaracter getXaracterId() {
        return this.xaracterId;
    }

    public void setXaracterId(Xaracter xaracter) {
        this.xaracterId = xaracter;
    }

    public XaracterSkill xaracterId(Xaracter xaracter) {
        this.setXaracterId(xaracter);
        return this;
    }

    public Skill getSkillId() {
        return this.skillId;
    }

    public void setSkillId(Skill skill) {
        this.skillId = skill;
    }

    public XaracterSkill skillId(Skill skill) {
        this.setSkillId(skill);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof XaracterSkill)) {
            return false;
        }
        return id != null && id.equals(((XaracterSkill) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "XaracterSkill{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            ", skillModifier=" + getSkillModifier() +
            "}";
    }
}
