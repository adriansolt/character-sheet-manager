package com.adi.cms.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A XaracterSkill.
 */
@Table("xaracter_skill")
public class XaracterSkill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("points")
    private Integer points;

    @Column("skill_modifier")
    private Integer skillModifier;

    @Transient
    @JsonIgnoreProperties(value = { "notes", "xaracterAttributes", "xaracterSkills", "user" }, allowSetters = true)
    private Xaracter xaracterId;

    @Transient
    @JsonIgnoreProperties(value = { "xaracterSkills", "defaultSkillOrAtributes", "prereqSkillOrAtributes" }, allowSetters = true)
    private Skill skillId;

    @Column("xaracter_id_id")
    private Long xaracterIdId;

    @Column("skill_id_id")
    private Long skillIdId;

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
        this.xaracterIdId = xaracter != null ? xaracter.getId() : null;
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
        this.skillIdId = skill != null ? skill.getId() : null;
    }

    public XaracterSkill skillId(Skill skill) {
        this.setSkillId(skill);
        return this;
    }

    public Long getXaracterIdId() {
        return this.xaracterIdId;
    }

    public void setXaracterIdId(Long xaracter) {
        this.xaracterIdId = xaracter;
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
