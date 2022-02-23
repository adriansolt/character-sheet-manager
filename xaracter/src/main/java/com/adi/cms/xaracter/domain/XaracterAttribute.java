package com.adi.cms.xaracter.domain;

import com.adi.cms.xaracter.domain.enumeration.AttributeName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A XaracterAttribute.
 */
@Entity
@Table(name = "xaracter_attribute")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class XaracterAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private AttributeName name;

    @NotNull
    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "attribute_modifier")
    private Integer attributeModifier;

    @ManyToOne
    @JsonIgnoreProperties(value = { "notes", "xaracterAttributes", "xaracterSkills", "user" }, allowSetters = true)
    private Xaracter xaracterId;

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
    }

    public XaracterAttribute xaracterId(Xaracter xaracter) {
        this.setXaracterId(xaracter);
        return this;
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
