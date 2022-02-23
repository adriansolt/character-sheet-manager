package com.adi.cms.gateway.domain;

import com.adi.cms.gateway.domain.enumeration.ArmorLocation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ArmorPiece.
 */
@Table("armor_piece")
public class ArmorPiece implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("location")
    private ArmorLocation location;

    @Column("defense_modifier")
    private Integer defenseModifier;

    @Transient
    @JsonIgnoreProperties(value = { "armorPiece" }, allowSetters = true)
    private Set<XaracterEquippedArmor> xaracterEquippedArmors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ArmorPiece id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArmorLocation getLocation() {
        return this.location;
    }

    public ArmorPiece location(ArmorLocation location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(ArmorLocation location) {
        this.location = location;
    }

    public Integer getDefenseModifier() {
        return this.defenseModifier;
    }

    public ArmorPiece defenseModifier(Integer defenseModifier) {
        this.setDefenseModifier(defenseModifier);
        return this;
    }

    public void setDefenseModifier(Integer defenseModifier) {
        this.defenseModifier = defenseModifier;
    }

    public Set<XaracterEquippedArmor> getXaracterEquippedArmors() {
        return this.xaracterEquippedArmors;
    }

    public void setXaracterEquippedArmors(Set<XaracterEquippedArmor> xaracterEquippedArmors) {
        if (this.xaracterEquippedArmors != null) {
            this.xaracterEquippedArmors.forEach(i -> i.setArmorPiece(null));
        }
        if (xaracterEquippedArmors != null) {
            xaracterEquippedArmors.forEach(i -> i.setArmorPiece(this));
        }
        this.xaracterEquippedArmors = xaracterEquippedArmors;
    }

    public ArmorPiece xaracterEquippedArmors(Set<XaracterEquippedArmor> xaracterEquippedArmors) {
        this.setXaracterEquippedArmors(xaracterEquippedArmors);
        return this;
    }

    public ArmorPiece addXaracterEquippedArmor(XaracterEquippedArmor xaracterEquippedArmor) {
        this.xaracterEquippedArmors.add(xaracterEquippedArmor);
        xaracterEquippedArmor.setArmorPiece(this);
        return this;
    }

    public ArmorPiece removeXaracterEquippedArmor(XaracterEquippedArmor xaracterEquippedArmor) {
        this.xaracterEquippedArmors.remove(xaracterEquippedArmor);
        xaracterEquippedArmor.setArmorPiece(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArmorPiece)) {
            return false;
        }
        return id != null && id.equals(((ArmorPiece) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArmorPiece{" +
            "id=" + getId() +
            ", location='" + getLocation() + "'" +
            ", defenseModifier=" + getDefenseModifier() +
            "}";
    }
}
