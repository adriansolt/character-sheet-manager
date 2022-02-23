package com.adi.cms.gateway.domain;

import com.adi.cms.gateway.domain.enumeration.AttributeName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A XaracterAttribute.
 */
@Table("xaracter_attribute")
public class XaracterAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private AttributeName name;

    @NotNull(message = "must not be null")
    @Column("points")
    private Integer points;

    @Column("attribute_modifier")
    private Integer attributeModifier;

    @Transient
    @JsonIgnoreProperties(value = { "notes", "xaracterAttributes", "xaracterSkills", "user" }, allowSetters = true)
    private Xaracter xaracterId;

    @Column("xaracter_id_id")
    private Long xaracterIdId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public XaracterAttribute id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AttributeName getName() {
        return this.name;
    }

    public XaracterAttribute name(AttributeName name) {
        this.setName(name);
        return this;
    }

    public void setName(AttributeName name) {
        this.name = name;
    }

    public Integer getPoints() {
        return this.points;
    }

    public XaracterAttribute points(Integer points) {
        this.setPoints(points);
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getAttributeModifier() {
        return this.attributeModifier;
    }

    public XaracterAttribute attributeModifier(Integer attributeModifier) {
        this.setAttributeModifier(attributeModifier);
        return this;
    }

    public void setAttributeModifier(Integer attributeModifier) {
        this.attributeModifier = attributeModifier;
    }

    public Xaracter getXaracterId() {
        return this.xaracterId;
    }

    public void setXaracterId(Xaracter xaracter) {
        this.xaracterId = xaracter;
        this.xaracterIdId = xaracter != null ? xaracter.getId() : null;
    }

    public XaracterAttribute xaracterId(Xaracter xaracter) {
        this.setXaracterId(xaracter);
        return this;
    }

    public Long getXaracterIdId() {
        return this.xaracterIdId;
    }

    public void setXaracterIdId(Long xaracter) {
        this.xaracterIdId = xaracter;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof XaracterAttribute)) {
            return false;
        }
        return id != null && id.equals(((XaracterAttribute) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "XaracterAttribute{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", points=" + getPoints() +
            ", attributeModifier=" + getAttributeModifier() +
            "}";
    }
}
